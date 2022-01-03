package com.buysell.market.Controller;

import com.buysell.market.DataObjects.Item;
import com.buysell.market.DataObjects.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for the Market API used for handling the buy and sell requests and creating orders
 *
 */

@RestController
@RequestMapping("/market")
public class MarketApiController {
    private final Logger logger = LoggerFactory.getLogger(MarketApiController.class);

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

        if(item == null) {
            logger.info("Market Buy| Requested item is null");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Invalid Item: null"
            );
        }
        if(item.getName() == null || item.getName().isEmpty()) {
            logger.info("Market Buy| Requested item: {} is null", item.getName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Invalid Item name: \"" + item.getName() + "\""
            );
        }
        if(item.getAmount() <= 0.0){
            logger.info("Market Buy| Requested item amount: {} is invalid", item.getAmount());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Invalid Item amount: " + item.getAmount()
            );
        }

        double amount = itemAmount(item.getName());
        if(amount < 0.0){
            logger.info("Market Buy| Requested item : {} doesn't exist in inventory", item.getName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Item Not Found: Item not carried Inventory: \"" + item.getName() + "\""
            );
        }


        if(amount < item.getAmount()) {
            logger.info("Market Buy| Requested item : Stock to Low: \"{}\", Requested Amount: {}, Inventory Amount: {}",
                    item.getName(), item.getAmount(), amount);
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "Purchase Not Processed: Stock to Low: \"" + item.getName() + "\", Requested Amount: "
                    + item.getAmount() + ", Inventory Amount: " + amount
            );
        }

        // Calculate total price
        double price = itemPrice(item.getName());
        if(price < 0.0){
            logger.warn("Market Buy| Requested item : {}, no price available", item.getName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Item Not Found: No Price available: \"" + item.getName() + "\""
            );
        }

        //Updating inventory database
        item.setAmount(item.getAmount()*-1);

        itemAmountUpdate(item);

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

        if(item == null) {
            logger.info("Market Buy| Requested item is null");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Invalid Item: null"
            );
        }
        if(item.getName() == null || item.getName().isEmpty()) {
            logger.info("Market Buy| Requested item: {} is null", item.getName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Invalid Item name: \"" + item.getName() + "\""
            );
        }

        if(item.getAmount() <= 0.0){
            logger.info("Market Buy| Requested item amount: {} is invalid", item.getAmount());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Invalid Item amount: " + item.getAmount()
            );
        }

        if(price < 0.0){
            logger.warn("Market Buy| Requested item : {}, no price available", item.getName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Item Not Found: No Price available: \"" + item.getName() + "\""
            );
        }

        //New item we don't recognise - what do
        if(amount < 0.0){
            logger.info("Market Buy| Requested item : {} doesn't exist in inventory", item.getName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Item Not Found: Item not carried Inventory: \"" + item.getName() + "\""
            );
        }

        //Updating inventory database
        itemAmountUpdate(item);

        double totalCost = price * amount;

        //Send to order fulfilment
        return null;
    }

    /**
     * Fetches the price of a given item.
     * @param itemName
     * @return double price
     */

    private static double itemPrice(String itemName) throws ResponseStatusException{
        Logger logger = LoggerFactory.getLogger(MarketApiController.class);

        try {
            logger.info("Market Item Amount| item: {}", itemName);
            RestTemplate restTemplate = new RestTemplate();
            Double price = restTemplate.getForObject("http://localhost:8080/price/{itemName}", Double.class, itemName);

            if (price == null){
                price = -1.0;
            }
            return price;

        } catch(RestClientException e){
            logger.error("Market Item Amount| RestClientException: {}", e.toString());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e
            );
        }
    }

    /**
     * Fetches the amount of a given item in the inventory.
     * @param itemName
     * @return double amount
     */
    private static double itemAmount(String itemName)  throws ResponseStatusException{
        Logger logger = LoggerFactory.getLogger(MarketApiController.class);

        try {
            logger.info("Market Item Amount| item: {}", itemName);
            RestTemplate restTemplate = new RestTemplate();
            Double amount = restTemplate.getForObject("http://localhost:8081/inventory/{itemName}", Double.class, itemName);

            if (amount == null){
                amount = -1.0;
            }
            return amount;

        } catch(RestClientException e){
            logger.error("Market Item Amount| RestClientException: {}", e.toString());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e
            );
        }


    }

    private static void itemAmountUpdate(Item item)  throws ResponseStatusException{
        Logger logger = LoggerFactory.getLogger(MarketApiController.class);

        try {
            logger.info("Market Item Amount Update| item: {}", item);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject("http://localhost:8081/inventory/update", item, String.class);


        } catch(RestClientException e){
            logger.error("Market Item Amount Update| RestClientException: {}", e.toString());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e
            );
        }


    }


}
