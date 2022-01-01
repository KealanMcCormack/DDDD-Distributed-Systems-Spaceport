package client.items;

import client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class PriceAPIRequests {

    static Logger logger = LoggerFactory.getLogger(Client.class);

    public static Item[] browse(int pageNo, int maxPageLength, String priceApiHost, String priceApiPort) throws RestClientException {
        logger.info("Browse| host: {}, port: {}, pageNo: {}, max: {}", priceApiHost, priceApiPort, pageNo, maxPageLength);
        RestTemplate restTemplate = new RestTemplate();
        Item[] items =
                restTemplate.getForObject("http://localhost:8080/price/browse?page={pageNo}&max={maxPageLength}",
                        Item[].class, pageNo, priceApiHost, priceApiPort, maxPageLength);

        return items;
    }

    public static Item[] browseAll(String priceApiHost, String priceApiPort) throws RestClientException{

        logger.info("Browse All| host: {}, port: {}", priceApiHost, priceApiPort);
        RestTemplate restTemplate = new RestTemplate();
        Item[] items =
                restTemplate.getForObject("http://{priceApiHost}:{priceApiPort}/price/browse/all", //"http://{priceApiHost}/price/browse/all",
                        Item[].class, priceApiHost, priceApiPort);

        return items;
    }

}
