package client;


import client.cargo.ShipCargo;
import client.items.InventoryAPIRequests;
import client.items.PriceAPIRequests;
import client.items.Item;
import client.market.MarketAPIRequests;
import client.shoppinglist.ShoppingList;
import client.uri.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Client for sent REST POST request for applications for clients to Space Port
 *
 * @author John O'Donnell
 */
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final String clientID;
    private ShipCargo shipCargo;

    private Map<String, UriInfo> uriMap;

    private double balance;



    public static void main(String[] args) {
        Item[] items;
        try {
            System.out.println("testing method");


            //items = Browse.browseAll("localhost", "8080");
            /*
            items = PriceAPIRequests.browse(0, 20, "localhost", "8080");

            for (Item item: items){
                System.out.println(item);
            }

            Item priceItem = PriceAPIRequests.itemPrice(new Item("item"), "localhost", "8080");
            System.out.println("Price: " + priceItem.getPrice());*/
        }
        catch (RestClientException e){
            logger.error("Client| Exception| msg: {}, e: {}", e.getLocalizedMessage(), e.toString());

        }

    }

    public Client(String clientID, ShipCargo cargo, double startMoney, Map<String, UriInfo> uriMap){
        this.clientID = clientID;
        this.shipCargo = cargo;
        this.balance = startMoney;
        this.uriMap = uriMap;

        assert this.uriMap.containsKey("priceAPI");
        assert this.uriMap.containsKey("inventoryAPI");
        assert this.uriMap.containsKey("marketAPI");
    }

    public List<Item> shop(ShoppingList shoppingList) throws InterruptedException {
        List<Item> unattainedItems = new ArrayList<>();

        shoppingList.removeCompletedItems();

        while (!shoppingList.listComplete()){

            for (Item desiredItem: shoppingList.getDesiredItems()) {
                double offeredPrice = PriceAPIRequests.itemPrice(desiredItem, uriMap.get("priceAPI"));
                if (offeredPrice > 0) {

                    if (desiredItem.getAmount() > 0) {
                        if (offeredPrice > desiredItem.getPrice()) {
                            logger.warn("Client Shop Buy| Offered price too expensive| Desired item: {}, Offered Price: {}", desiredItem, offeredPrice);
                            unattainedItems.add(shoppingList.removeListItem(desiredItem.getName()));
                        }
                        else if(balance <= 0){
                            logger.warn("Client Shop Buy| Out of Funds| Desired item: {}", desiredItem);
                            unattainedItems.add(shoppingList.removeListItem(desiredItem.getName()));
                        }
                        else {

                            double availableAmount = InventoryAPIRequests.itemAmount(desiredItem, uriMap.get("inventoryAPI"));

                            if (availableAmount > 0) {
                                double buyAmount = Math.min(availableAmount, desiredItem.getAmount());
                                buyAmount = Math.min(buyAmount, balance / offeredPrice);

                                MarketAPIRequests.buy(desiredItem.getName(), buyAmount, clientID, uriMap.get("marketAPI"));

                                logger.info("Client Shop Buy| Buying {}, amount: {}, price: {}, cost: {}| Desired item: {}",
                                        desiredItem.getName(), buyAmount, offeredPrice, buyAmount * offeredPrice, desiredItem);
                                shoppingList.updateListItem(buyAmount * -1, desiredItem.getName());
                                balance -= buyAmount * offeredPrice;
                            } else {
                                logger.warn("Client Shop Buy| No availability of item type to buy| Desired item: {}", desiredItem);
                                unattainedItems.add(shoppingList.removeListItem(desiredItem.getName()));
                            }
                        }
                    } else if (shipCargo.getItem(desiredItem.getName()).getAmount() > 0) {
                        if (offeredPrice < desiredItem.getPrice()) {
                            logger.warn("Client Shop Sell| Offered price too low| Desired item: {}, Offered Price: {}", desiredItem, offeredPrice);
                            unattainedItems.add(shoppingList.removeListItem(desiredItem.getName()));
                        }
                        else {
                            double sellAmount = Math.min(shipCargo.getItem(desiredItem.getName()).getAmount(), desiredItem.getAmount());
                            MarketAPIRequests.sell(desiredItem.getName(), sellAmount, clientID, uriMap.get("marketAPI"));

                            logger.info("Client Shop Sell| Selling {}, amount: {}, price: {}, profit: {}| Desired item: {}",
                                    desiredItem.getName(), sellAmount, offeredPrice, sellAmount * offeredPrice, desiredItem);
                            shoppingList.updateListItem(sellAmount, desiredItem.getName());
                            balance += sellAmount * offeredPrice;
                        }
                    } else{
                        logger.warn("Client Shop Sell| No Cargo of item type to sell| Desired item: {}", desiredItem);
                        unattainedItems.add(shoppingList.removeListItem(desiredItem.getName()));
                    }
                }
                else{
                    logger.warn("Client Shop| No Price of item type to sell, Item no available at Space Port| Desired item: {}", desiredItem);
                    unattainedItems.add(shoppingList.removeListItem(desiredItem.getName()));
                }
            }
            shoppingList.removeCompletedItems();
            wait(1000);
        }
        return unattainedItems;
    }


}