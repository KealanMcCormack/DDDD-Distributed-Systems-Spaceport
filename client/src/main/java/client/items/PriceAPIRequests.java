package client.items;

import client.exceptions.InvalidResponseException;
import client.uri.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class PriceAPIRequests {

    private final static Logger logger = LoggerFactory.getLogger(PriceAPIRequests.class);


    /**
     * Price API Browse Paginated Request
     *
     * @param pageNo Page No.
     * @param maxPageLength Max length of page
     * @param uriInfo URI info for API
     * @return Array of Items
     * @throws RestClientException
     */
    public static Item[] browse(int pageNo, int maxPageLength, UriInfo uriInfo) throws RestClientException {
        logger.info("Browse| uri: {}, pageNo: {}, max: {}", uriInfo, pageNo, maxPageLength);
        RestTemplate restTemplate = new RestTemplate();
        Item[] items =
                restTemplate.getForObject("http://{priceApiHost}:{priceApiPort}/price/browse?page={pageNo}&max={maxPageLength}",
                        Item[].class, uriInfo.getHost(), uriInfo.getPort(),  pageNo, maxPageLength);

        return items;
    }

    /**
     * Price API Browse All Request
     *
     * @param uriInfo URI info for API
     * @return Array of Items
     * @throws RestClientException
     */
    public static Item[] browseAll(UriInfo uriInfo) throws RestClientException{

        logger.info("Browse All| uri: {}", uriInfo);
        RestTemplate restTemplate = new RestTemplate();
        Item[] items =
                restTemplate.getForObject("http://{priceApiHost}:{priceApiPort}/price/browse/all",
                        Item[].class, uriInfo.getHost(), uriInfo.getPort());

        logger.info("Browse All| noItems: {}, ", items.length);

        return items;
    }

    /**
     * Inventory API Price Request
     *
     * @param item Item to get inventory amount
     * @param uriInfo URI info for API
     * @return Array of Items
     * @throws RestClientException
     * @throws InvalidResponseException
     */
    public static double itemPrice(Item item, UriInfo uriInfo)
            throws RestClientException, InvalidResponseException {

        logger.info("Item Price| uri: {}, item: {}", uriInfo, item);
        RestTemplate restTemplate = new RestTemplate();

        Double price =
                restTemplate.getForObject("http://{priceApiHost}:{priceApiHost}/price/{}}",
                        Double.class, uriInfo.getHost(), uriInfo.getPort(), item.getName());

        if (price == null){
            throw new InvalidResponseException("Item Amount Response Null");
        }

        return price;
    }

}
