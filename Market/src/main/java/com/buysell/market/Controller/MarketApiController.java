package com.buysell.market.Controller;

import com.buysell.market.DataObjects.Item;
import com.buysell.market.DataObjects.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Controller for the Market API used for handling the buy and sell requests and creating orders
 *
 */

@RestController
@RequestMapping("/market")
public class MarketApiController {
    final Logger logger = LoggerFactory.getLogger(MarketApiController.class);

    //Notes
    // Careful about localhosts - for the moment Price is 8080, inventory is 8081

    /**
     * Post Mapping for buying an item from the inventory. /buy
     * @param item
     * @return null
     */
    //Should take in customer id
    @PostMapping("/buy")
    String buyItem(@RequestBody Item item, @RequestBody int customerID) {

        if(item.getName().isEmpty()) {
            logger.info("Requested item : " + item + " is null");
            return "There seems to be something wrong with the name";
        }

        if(itemAmount(item.getName()) < 0.0){
            logger.warn("Requested item : " + item + " doesn't exist in inventory");
            return "This item isn't in stock at the moment";
        }

        double amount = itemAmount(item.getName());
        if(amount < 0.0 ) {
            logger.info("Requested item : " + item + " doesn't have required stocks");
            return "Sorry to say we ain't got enough a dis";
        }

        // Calculate total price
        double price = itemPrice(item.getName());
        if(price < 0.0){
            logger.warn("Requested item : " + item + " had a price returned < 0");
            return("Sorry, we couldn't find a price for that item. Please check back later");
        }

        //Updating inventory database
        item.setAmount(item.getAmount()*-1);

        final String uri = "http://localhost:8081/inventory/update";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(uri, item, String.class);

        double totalCost = price * amount;


        //Send Order to order fulfilment ( Order Request from core )

        //Order Id should be safe, as this application can be distributed


        return null;
    }

    /**
     * Post Mapping for selling an item to the space port's inventory. /sell
     * @param item
     * @return null
     */
    //Take customer id
    @PostMapping("/sell")
    String sellItem(@RequestBody Item item, @RequestBody int customerID) {
        double price = itemPrice(item.getName());
        double amount = itemAmount(item.getName());

        if(price < 0.0){
            logger.warn("Requested item : " + item + " had a price returned < 0");
            return("Sorry, we couldn't find a price for that item. Please check back later");
        }

        //New item we don't recognise - what do
        if(itemAmount(item.getName()) < 0.0){
            logger.warn("Requested item : " + item + " doesn't exist in inventory");
            return "This port does not trade with this item";
        }

        //Updating inventory database
        final String uri = "http://localhost:8081/inventory/update";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(uri, item, String.class);

        double totalCost = price * amount;

        //Send to order fulfilment
        return null;
    }

    /**
     * Fetches the price of a given item.
     * @param itemName
     * @return double price
     */

    private static double itemPrice(String itemName)
    {
        final String uri = "http://localhost:8080/price/" + itemName;

        RestTemplate restTemplate = new RestTemplate();
        double price = -1.0;
        String result = restTemplate.getForObject(uri, String.class);

        try{
            assert result != null;
            price = Double.parseDouble(result);
        } catch(Exception e){
            System.out.println("Item Pricing failed : " + e);
        }

        return price;
    }

    /**
     * Fetches the amount of a given item in the inventory.
     * @param itemName
     * @return double amount
     */

    private static double itemAmount(String itemName)
    {
        final String uri = "http://localhost:8081/inventory/" + itemName;

        RestTemplate restTemplate = new RestTemplate();
        double amount = -1.0;
        String result = restTemplate.getForObject(uri, String.class);

        try{
            assert result != null;
            amount = Double.parseDouble(result);
        } catch(Exception e){
            System.out.println("Item Amount failed : " + e);
        }

        return amount;
    }


}
