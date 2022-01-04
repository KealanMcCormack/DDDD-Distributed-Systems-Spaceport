import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {


    public static void main (String... args) {
        try{
            parsefile();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    static void parsefile() throws FileNotFoundException {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/main/resources/items.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray itemList = (JSONArray) obj;

            //Iterate over employee array
            itemList.forEach( item -> parseItemObject( (JSONObject) item ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static class PriceItem {
        String name;
        double price;

        public PriceItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    private static class InventoryItem {
        String name;

        public InventoryItem(String name, double amount) {
            this.name = name;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        double amount;
    }

    private static void parseItemObject(JSONObject item)
    {
        JSONObject itemObject = (JSONObject) item.get("item");

        String name = (String) itemObject.get("name");

        double price = (Double) itemObject.get("price");

        double amount = (Double) itemObject.get("amount");

        PriceItem priceItem = new PriceItem(name, price);

        InventoryItem inventoryItem = new InventoryItem(name, amount);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://price-api:8081/price/add", priceItem, String.class);

        restTemplate.postForObject("http://inventory-api:8081/inventory/update", inventoryItem, String.class);
    }
}
