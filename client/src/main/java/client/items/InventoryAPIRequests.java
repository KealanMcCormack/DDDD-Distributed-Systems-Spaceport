package client.items;

import client.exceptions.InvalidResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class InventoryAPIRequests {

    private static Logger logger = LoggerFactory.getLogger(InventoryAPIRequests.class);

    /**
     * Inventory API Amount Request
     *
     * @param item Item to get inventory amount
     * @param inventoryApiHost Inventory API Host Address
     * @param inventoryApiPort Inventory API Host Port
     * @return Array of Items
     * @throws RestClientException
     * @throws InvalidResponseException
     */
    public static Item itemAmount(Item item, String inventoryApiHost, String inventoryApiPort)
            throws RestClientException, InvalidResponseException {

        logger.info("Item Amount| host: {}, port: {}, item: {}", inventoryApiHost, inventoryApiPort, item);
        RestTemplate restTemplate = new RestTemplate();

        Double amount =
                restTemplate.getForObject("http://{inventoryApiHost}:{inventoryApiPort}/inventory/{}}",
                        Double.class, inventoryApiHost, inventoryApiPort, item.getName());

        if (amount == null){
            throw new InvalidResponseException("Item Amount Response Null");
        }
        else {
            item.setAmount(amount);
        }
        return item;
    }

    /**
     * Inventory API Browse All Request
     *
     * @param inventoryApiHost Inventory API Host Address
     * @param inventoryApiPort Inventory API Host Port
     * @return Array of Items
     * @throws RestClientException
     */
    public static Item[] itemAmountsAll(String inventoryApiHost, String inventoryApiPort) throws RestClientException{

        logger.info("Item Amount All| host: {}, port: {}", inventoryApiHost, inventoryApiPort);
        RestTemplate restTemplate = new RestTemplate();
        Item[] items =
                restTemplate.getForObject("http://{inventoryApiHost}:{inventoryApiPort}/price/browse/all",
                        Item[].class, inventoryApiHost, inventoryApiPort);

        logger.info("Item Amount All| noItems: {}", items.length);

        return items;
    }

}
