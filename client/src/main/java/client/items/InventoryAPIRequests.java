package client.items;

import client.exceptions.InvalidResponseException;
import client.uri.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class InventoryAPIRequests {

    private final static Logger logger = LoggerFactory.getLogger(InventoryAPIRequests.class);

    /**
     * Inventory API Amount Request
     *
     * @param item Item to get inventory amount
     * @param uriInfo URI info for API
     * @return Array of Items
     * @throws RestClientException
     * @throws InvalidResponseException
     */
    public static double itemAmount(Item item, UriInfo uriInfo)
            throws RestClientException, InvalidResponseException {

        logger.info("Item Amount| uri: {}, item: {}", uriInfo, item);
        RestTemplate restTemplate = new RestTemplate();

        Double amount =
                restTemplate.getForObject("http://{inventoryApiHost}:{inventoryApiPort}/inventory/{}}",
                        Double.class, uriInfo.getHost(), uriInfo.getPort(), item.getName());

        if (amount == null){
            throw new InvalidResponseException("Item Amount Response Null");
        }

        return amount;
    }

    /**
     * Inventory API Item All Request
     *
     * @param uriInfo URI info for API
     * @return Array of Items
     * @throws RestClientException
     */
    public static Item[] itemAmountsAll(UriInfo uriInfo) throws RestClientException{

        logger.info("Item Amount All| uri: {}", uriInfo);
        RestTemplate restTemplate = new RestTemplate();
        Item[] items =
                restTemplate.getForObject("http://{inventoryApiHost}:{inventoryApiPort}/price/browse/all",
                        Item[].class, uriInfo.getHost(), uriInfo.getPort());

        logger.info("Item Amount All| noItems: {}", items.length);

        return items;
    }

    /**
     * Inventory API Amount Request on List of Items
     *
     * @param items Items to get inventory amount
     * @param uriInfo URI info for API
     * @return Array of Items
     * @throws RestClientException
     * @throws InvalidResponseException
     */
    public static Item[] itemAmountsList(Item[] items, UriInfo uriInfo)
            throws RestClientException, InvalidResponseException {

        logger.info("Item Amount List| uri: {}, items size: {}", uriInfo, items.length);
        for (int i = 0; i < items.length; i++) {
            items[i].setAmount(itemAmount(items[i], uriInfo));
        }

        return items;
    }

    /**
     * Inventory API Amount Request and check has amount
     *
     * @param item Items to get inventory amount
     * @param amount Amount to the item to check
     * @param uriInfo URI info for API
     * @return Array of Items
     * @throws RestClientException
     * @throws InvalidResponseException
     */
    public static boolean hasItemAmount(Item item, double amount, UriInfo uriInfo)
            throws RestClientException, InvalidResponseException {
        logger.info("Has Item Amount| uri: {}, items: {}, amount: {}", uriInfo, item, amount);
        double responseAmount = itemAmount(item, uriInfo);

        return responseAmount >= amount;
    }

}
