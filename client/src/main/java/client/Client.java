package client;


import client.cargo.ShipCargo;
import client.items.InventoryAPIRequests;
import client.items.Item;
import client.items.PriceAPIRequests;
import client.market.MarketAPIRequests;
import client.shoppinglist.ShoppingList;
import client.uri.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Client for sent REST POST request for applications for clients to Space Port
 *
 * @author John O'Donnell
 */
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final String clientID;
    private final ShipCargo shipCargo;
    private final Map<String, UriInfo> uriMap;
    private double balance;



    public static void main(String[] args) {
        String id = "1234";
        double money = 1000;
        try {

            // Flag parsing
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--help":
                        System.out.println(helpMessage());
                        System.exit(0);
                    case "-id":
                        if (i + 1 < args.length) {
                            id = args[++i];
                            break;
                        }
                        else{
                            logger.error("Client| No ID argument provided for -id flag| args: {}", Arrays.toString(args));
                            System.out.println(helpMessage());
                            System.exit(0);
                        }
                    case "--money":
                    case "-m":
                        if (i + 1 < args.length) {
                            try {
                                money = Double.parseDouble(args[++i]);
                            }
                            catch (NumberFormatException e){
                                logger.error("Client| Money argument provided is not double| arg: {}, args: {}", args[i], Arrays.toString(args));
                                System.out.println(helpMessage());
                                System.exit(0);
                            }
                            break;
                        }
                        else{
                            logger.error("Client| No money argument provided for --money flag| args: {}", Arrays.toString(args));
                            System.out.println(helpMessage());
                            System.exit(0);
                        }
                    default:
                        logger.error("Client| Unexpected arg: {}, args: {}", args[i], Arrays.toString(args));
                        System.out.println(helpMessage());
                        System.exit(0);
                }
            }

            Map<String, UriInfo> uriMap = new HashMap<>();
            uriMap.put("priceAPI", new UriInfo("localhost", "8080"));
            uriMap.put("inventoryAPI", new UriInfo("localhost", "8081"));
            uriMap.put("marketAPI", new UriInfo("localhost", "8084"));

            ShipCargo cargo = new ShipCargo();
            cargo.add(new Item("iron", -1, 50));
            cargo.add(new Item("cheese", -1, 20));


            Client client = new Client(id, cargo, money, uriMap);

            Map<String, Item> shopMap = new HashMap<>();
            shopMap.put("aaaaa", new Item("aaaaa", 120, 6));
            shopMap.put("iron", new Item("iron", 6, -3));
            shopMap.put("steel", new Item("steel", 120, 6));
            ShoppingList shopList = new ShoppingList(shopMap);

            List<Item> unattainedItems = client.shop(shopList);

            //Print
            System.out.println("Unattained Items: " + unattainedItems);

            System.out.println(client);


        }
        catch (Exception e){
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

    /**
     * Client method to Buy and Sell to the Space Port based on Shopping List
     *
     * @param shoppingList shopping list of Items to buy
     * @return List of unattained Items
     * @throws InterruptedException
     */
    public List<Item> shop(ShoppingList shoppingList) throws InterruptedException {
        List<Item> unattainedItems = new ArrayList<>();

        shoppingList.removeCompletedItems();

        while (!shoppingList.listComplete()){

            Iterator<Map.Entry<String, Item>> entryIterator = shoppingList.getDesiredItemsIterator();
            //Iterator<Item> itemIterator = shoppingList.getDesiredItems().iterator();
            //for (Item desiredItem: shoppingList.getDesiredItems().) {
            while (entryIterator.hasNext()){
                Map.Entry<String, Item> next = entryIterator.next();
                Item desiredItem = next.getValue();
                double offeredPrice = PriceAPIRequests.itemPrice(desiredItem, uriMap.get("priceAPI"));
                if (offeredPrice > 0) {

                    if (desiredItem.getAmount() > 0) {
                        if (offeredPrice > desiredItem.getPrice()) {
                            logger.warn("Client Shop Buy| Offered price too expensive| Desired item: {}, Offered Price: {}", desiredItem, offeredPrice);

                            unattainedItems.add(desiredItem);
                            entryIterator.remove();
                        }
                        else if(balance <= 0){
                            logger.warn("Client Shop Buy| Out of Funds| Desired item: {}", desiredItem);
                            unattainedItems.add(desiredItem);
                            entryIterator.remove();
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
                                if (shipCargo.getItem(desiredItem.getName()) != null){
                                    shipCargo.updateAmount(buyAmount, desiredItem.getName());
                                }
                                else{
                                    shipCargo.add(new Item(desiredItem.getName(), -1, buyAmount));
                                }

                                balance -= buyAmount * offeredPrice;
                            } else {
                                logger.warn("Client Shop Buy| No availability of item type to buy| Desired item: {}", desiredItem);
                                unattainedItems.add(desiredItem);
                                entryIterator.remove();
                            }
                        }
                    } else if (shipCargo.getItem(desiredItem.getName()) != null && shipCargo.getItem(desiredItem.getName()).getAmount() > 0) {
                        if (offeredPrice < desiredItem.getPrice()) {
                            logger.warn("Client Shop Sell| Offered price too low| Desired item: {}, Offered Price: {}", desiredItem, offeredPrice);
                            unattainedItems.add(desiredItem);
                            entryIterator.remove();
                        }
                        else {
                            double sellAmount = Math.min(shipCargo.getItem(desiredItem.getName()).getAmount(), desiredItem.getAmount()*-1);
                            MarketAPIRequests.sell(desiredItem.getName(), sellAmount, clientID, uriMap.get("marketAPI"));

                            logger.info("Client Shop Sell| Selling {}, amount: {}, price: {}, profit: {}| Desired item: {}",
                                    desiredItem.getName(), sellAmount, offeredPrice, sellAmount * offeredPrice, desiredItem);
                            shoppingList.updateListItem(sellAmount, desiredItem.getName());
                            shipCargo.updateAmount(-1*sellAmount, desiredItem.getName());
                            balance += sellAmount * offeredPrice;
                        }
                    } else{
                        logger.warn("Client Shop Sell| No Cargo of item type to sell| Desired item: {}", desiredItem);
                        unattainedItems.add(desiredItem);
                        entryIterator.remove();
                    }
                }
                else{
                    logger.warn("Client Shop| No Price of item type to sell, Item no available at Space Port| Desired item: {}", desiredItem);
                    unattainedItems.add(desiredItem);
                    entryIterator.remove();
                }
            }
            shoppingList.removeCompletedItems();
        }
        return unattainedItems;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientID='" + clientID + '\'' +
                ", shipCargo=" + shipCargo +
                ", uriMap=" + uriMap +
                ", balance=" + balance +
                '}';
    }

    public static String helpMessage(){
        //TODO
        return "TODO";
    }


}