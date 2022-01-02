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
     * @param inventoryApiHost Price API Host Address
     * @param inventoryApiPort Price API Host Port
     * @return Array of Items
     * @throws RestClientException
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
}
