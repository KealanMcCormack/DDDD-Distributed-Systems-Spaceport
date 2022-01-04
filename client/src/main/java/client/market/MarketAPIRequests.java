package client.market;

import client.items.Item;
import client.uri.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class MarketAPIRequests {

    private final static Logger logger = LoggerFactory.getLogger(MarketAPIRequests.class);

    public static void buy(String itemName, double amount, String id, UriInfo uriInfo)
            throws RestClientException{
        logger.info("Market Buy| uri: {}, item: {}, amount: {}, ID{}", uriInfo, itemName, amount, id);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Item> request = new HttpEntity<>(new Item(itemName, -1, amount));

        restTemplate.postForObject("http://{priceApiHost}:{priceApiPort}/market/buy/{id}",
                request, String.class, uriInfo.getHost(), uriInfo.getPort(), id);
    }

    public static void sell(String itemName, double amount, String id, UriInfo uriInfo)
            throws RestClientException{
        logger.info("Market Sell| uri: {}, item: {}, amount: {}, ID{}", uriInfo, itemName, amount, id);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Item> request = new HttpEntity<>(new Item(itemName, -1, amount));

        restTemplate.postForObject("http://{priceApiHost}:{priceApiPort}/market/sell/{id}",
                request, String.class, uriInfo.getHost(), uriInfo.getPort(), id);
    }
}
