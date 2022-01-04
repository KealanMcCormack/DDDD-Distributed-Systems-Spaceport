package com.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import messages.PriceItem;
import messages.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@NoArgsConstructor
public class PriceMonitoringService {
    RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PriceMonitoringService.class);

    public void priceChange(Product product){
        Double amount = restTemplate.getForObject("http://localhost:8081/inventory/" + product.getName(), Double.class);

        if(amount == null || amount == -1.0) {
            logger.warn("Amount returned was : " + amount + " , stopping action");
            return;
        }

        Double price = restTemplate.getForObject("http://localhost:8081/price/" + product.getName(), Double.class);

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
        final String uri = "http://localhost:8080/price/update";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(uri, priceItem, String.class);
    }

    private void increasePrice(PriceItem priceItem){
        final String uri = "http://localhost:8080/price/update";
        logger.info("Increasing price by : " + priceItem.getPrice());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(uri, priceItem, String.class);
    }
}
