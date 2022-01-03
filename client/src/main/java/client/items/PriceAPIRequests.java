package client.items;

import client.Client;
import client.exceptions.InvalidResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class PriceAPIRequests {

    private static Logger logger = LoggerFactory.getLogger(PriceAPIRequests.class);


    /**
     * Price API Browse Paginated Request
     *
     * @param pageNo Page No.
     * @param maxPageLength Max length of page
     * @param priceApiHost Price API Host Address
     * @param priceApiPort Price API Host Port
     * @return Array of Items
     * @throws RestClientException
     */
    public static Item[] browse(int pageNo, int maxPageLength, String priceApiHost, String priceApiPort) throws RestClientException {
        logger.info("Browse| host: {}, port: {}, pageNo: {}, max: {}", priceApiHost, priceApiPort, pageNo, maxPageLength);
        RestTemplate restTemplate = new RestTemplate();
        Item[] items =
                restTemplate.getForObject("http://{priceApiHost}:{priceApiPort}/price/browse?page={pageNo}&max={maxPageLength}",
                        Item[].class, priceApiHost, priceApiPort,  pageNo, maxPageLength);

        return items;
    }

    /**
     * Price API Browse All Request
     *
     * @param priceApiHost Price API Host Address
     * @param priceApiPort Price API Host Port
     * @return Array of Items
     * @throws RestClientException
     */
    public static Item[] browseAll(String priceApiHost, String priceApiPort) throws RestClientException{

        logger.info("Browse All| host: {}, port: {}", priceApiHost, priceApiPort);
        RestTemplate restTemplate = new RestTemplate();
        Item[] items =
                restTemplate.getForObject("http://{priceApiHost}:{priceApiPort}/price/browse/all",
                        Item[].class, priceApiHost, priceApiPort);

        logger.info("Browse All| noItems: {}, ", items.length);

        return items;
    }

    /**
     * Inventory API Price Request
     *
     * @param item Item to get inventory amount
     * @param priceApiHost Price API Host Address
     * @param priceApiPort Price API Host Port
     * @return Array of Items
     * @throws RestClientException
     * @throws InvalidResponseException
     */
    public static Item itemPrice(Item item, String priceApiHost, String priceApiPort)
            throws RestClientException, InvalidResponseException {

        logger.info("Item Price| host: {}, port: {}, item: {}", priceApiHost, priceApiPort, item);
        RestTemplate restTemplate = new RestTemplate();

        Double price =
                restTemplate.getForObject("http://{priceApiHost}:{priceApiHost}/price/{}}",
                        Double.class, priceApiHost, priceApiPort, item.getName());

        if (price == null){
            throw new InvalidResponseException("Item Amount Response Null");
        }
        else {
            item.setPrice(price);
        }

        return item;
    }


    public static void add(Item item, String priceApiHost, String priceApiPort) throws RestClientException{
        logger.info("Add Item| item:{}, host: {}, port: {}", item, priceApiHost, priceApiPort);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Item> request = new HttpEntity<>(item);

        restTemplate.postForObject("http://{priceApiHost}:{priceApiPort}/price/add",
                request, void.class, priceApiHost, priceApiPort);
    }

}
