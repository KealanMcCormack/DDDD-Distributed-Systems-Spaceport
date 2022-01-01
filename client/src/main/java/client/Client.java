package client;


import client.items.PriceAPIRequests;
import client.items.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;


/**
 * Client for sent REST POST request for applications for clients to Space Port
 *
 * @author John O'Donnell
 */
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        Item[] items;
        try {


            //items = Browse.browseAll("localhost", "8080");

            items = PriceAPIRequests.browse(0, 20, "localhost", "8080");

            for (Item item: items){
                System.out.println(item);
            }
        }
        catch (RestClientException e){
            logger.error("Client| Exception| msg: {}, e: {}", e.getLocalizedMessage(), e.toString());

        }

    }

}