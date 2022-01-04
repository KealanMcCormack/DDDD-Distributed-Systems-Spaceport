package com.buysell.market.Controller;

import com.buysell.market.DataObjects.Item;
import messages.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${priceHost}")
    private String priceHost;

    @Value("${pricePort}")
    private String pricePort;

    @Value("${inventoryHost}")
    private String inventoryHost;

    @Value("${inventoryPort}")
    private String inventoryPort;

    @Value("${PriceMonitorHost}")
    private static String priceMonitorHost;

    @Value("${PriceMonitorPort}")
    private static String priceMonitorPort;

    /**
     * Post Mapping for buying an item from the inventory. /buy
     *
     * @param item
     * @param customerID
     * @return
     */
    @PostMapping("/buy/{customerID}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    String buyItem(@RequestBody Item item, @PathVariable String customerID) {

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

        double amount = itemAmount(item.getName(), inventoryHost, inventoryPort);
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
        double price = itemPrice(item.getName(), priceHost, pricePort);
        if(price < 0.0){
            logger.warn("Market Buy| Requested item : {}, no price available", item.getName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Item Not Found: No Price available: \"" + item.getName() + "\""
            );
        }

        //Updating inventory database
        item.setAmount(item.getAmount()*-1);

        itemAmountUpdate(item, inventoryHost, inventoryPort);

        double totalCost = price * amount;

        priceMonitor(new Product(item.getName(), item.getAmount(), totalCost));
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
    @PostMapping("/sell/{customerID}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    String sellItem(@RequestBody Item item, @PathVariable String customerID) {
        double price = itemPrice(item.getName(), priceHost, pricePort);
        double amount = itemAmount(item.getName(), inventoryHost, inventoryPort);

        if(item == null) {
            logger.info("Market Sell| Requested item is null");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Invalid Item: null"
            );
        }
        if(item.getName() == null || item.getName().isEmpty()) {
            logger.info("Market Sell| Requested item: {} is null", item.getName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Invalid Item name: \"" + item.getName() + "\""
            );
        }

        if(item.getAmount() <= 0.0){
            logger.info("Market Sell| Requested item amount: {} is invalid", item.getAmount());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Invalid Item amount: " + item.getAmount()
            );
        }

        if(price < 0.0){
            logger.warn("Market Sell| Requested item : {}, no price available", item.getName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Item Not Found: No Price available: \"" + item.getName() + "\""
            );
        }

        //New item we don't recognise - what do
        if(amount < 0.0){
            logger.info("Market Sell| Requested item : {} doesn't exist in inventory", item.getName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Item Not Found: Item not carried Inventory: \"" + item.getName() + "\""
            );
        }

        //Updating inventory database
        itemAmountUpdate(item, inventoryHost, inventoryPort);

        double totalCost = price * amount;

        priceMonitor(new Product(item.getName(), item.getAmount(), totalCost));

        //Send to order fulfilment
        return null;
    }

    /**
     * Fetches the price of a given item.
     * @param itemName
     * @return double price
     */

    private static double itemPrice(String itemName, String priceHost, String pricePort) throws ResponseStatusException{
        Logger logger = LoggerFactory.getLogger(MarketApiController.class);

        try {
            logger.info("Market Item Amount| item: {}, host: {}, port: {}", itemName, priceHost, pricePort);
            RestTemplate restTemplate = new RestTemplate();
            Double price = restTemplate.getForObject("http://{priceHost}:{pricePort}/price/{itemName}", Double.class, priceHost, pricePort, itemName);

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
    private static double itemAmount(String itemName, String inventoryHost, String inventoryPort)  throws ResponseStatusException{
        Logger logger = LoggerFactory.getLogger(MarketApiController.class);

        try {
            logger.info("Market Item Amount| item: {}, host: {}, port: {}", itemName, inventoryHost, inventoryPort);
            RestTemplate restTemplate = new RestTemplate();
            Double amount = restTemplate.getForObject("http://{inventoryHost}:{inventoryHost}/inventory/{itemName}", Double.class, inventoryHost, inventoryPort, itemName);

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

    private static void itemAmountUpdate(Item item, String inventoryHost, String inventoryPort)  throws ResponseStatusException{
        Logger logger = LoggerFactory.getLogger(MarketApiController.class);

        try {
            logger.info("Market Item Amount Update| item: {}, host: {}, port: {}", item, inventoryHost, inventoryPort);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject("http://{inventoryHost}:{inventoryPort}/inventory/update", item, String.class, inventoryHost, inventoryPort);


        } catch(RestClientException e){
            logger.error("Market Item Amount Update| RestClientException: {}", e.toString());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e
            );
        }
    }

    private static void priceMonitor(Product product){
        Logger logger = LoggerFactory.getLogger(MarketApiController.class);

        try {
            logger.info("Sending produt to have price adjusted: {}", product.getName());
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject("http://{hosts}:{ports}/monitor/pricechange", product, String.class, priceMonitorHost, priceMonitorPort);


        } catch(RestClientException e){
            logger.error("Failed to call price adjustment | RestClientException: {}", e.toString());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e
            );
        }
    }
}
