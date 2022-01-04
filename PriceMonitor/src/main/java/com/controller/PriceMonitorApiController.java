package com.controller;

import messages.PriceItem;
import messages.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/monitor")
public class PriceMonitorApiController {
    private final Logger logger = LoggerFactory.getLogger(PriceMonitorApiController.class);
    RestTemplate restTemplate = new RestTemplate();
    String PRICEAPIHOST = "http://price-api:8080/price/";
    String INVENTORYAPIHOST = "http://inventory-api:8081/inventory/";

    @PostMapping("/pricechange")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void priceChange(@RequestBody Product product){
        Double amount = restTemplate.getForObject(INVENTORYAPIHOST + product.getName(), Double.class);

        if(amount == null || amount == -1.0) {
            logger.warn("Amount returned was : " + amount + " , stopping action");
            return;
        }

        Double price = restTemplate.getForObject(PRICEAPIHOST + product.getName(), Double.class);

        if(price == null || price == -1.0) {
            logger.warn("Price returned was : " + price + " , stopping action");
            return;
        }

        double change = percentageCalc(amount, product.getAmount(), price);

        PriceItem priceItem = new PriceItem(product.getName(), change);
        if(product.getAmount() > 0){
            decreasePrice(priceItem);
        }else{
            increasePrice(priceItem);
        }
    }

    private double percentageCalc(double inventoryAmount, double productAmount, double price){
        double percentage = productAmount / inventoryAmount;

        logger.info("Percentage price change calculated : " + percentage);

        if(percentage >= 9.5){
            return price * .095;
        }

        if(percentage < 0){
            return 0;
        }

        return percentage * price;
    }

    private void decreasePrice(PriceItem priceItem){
        priceItem.setPrice(priceItem.getPrice() * -1.0);
        logger.info("Decreasing price by : " + priceItem.getPrice());
        final String uri = PRICEAPIHOST + "update";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(uri, priceItem, String.class);
    }

    private void increasePrice(PriceItem priceItem){
        final String uri = PRICEAPIHOST + "update";
        logger.info("Increasing price by : " + priceItem.getPrice());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(uri, priceItem, String.class);
    }



}
