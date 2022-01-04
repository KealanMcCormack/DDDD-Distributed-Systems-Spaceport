package client.cargo;

import client.items.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ShipCargo {

    private Map<String, Item> cargoMap;

    public ShipCargo(){
        cargoMap = new HashMap<>();
    }

    public ShipCargo(Map cargoMap){
        this.cargoMap = cargoMap;
    }

    public void add(Item item) throws IllegalArgumentException{
        if (item == null){
            throw new IllegalArgumentException("Null Item passed");
        }
        else if(item.getName() == null){
            throw new IllegalArgumentException("Invalid Item name| name: " + item.getName());
        }
        else if(item.getAmount() < 0){
            throw new IllegalArgumentException("Invalid Item amount, cant be negative| amount: " + item.getAmount());
        }
        else if(cargoMap.containsKey(item.getName())){
            throw new IllegalArgumentException("Item already exists in cargo map| item: " + cargoMap.get(item.getName()));
        }

        cargoMap.put(item.getName(), item);
    }

    public void updateAmount(double amount, String itemName) throws IllegalArgumentException, NoSuchElementException{
        if (itemName == null){
            throw new IllegalArgumentException("Invalid Item name| name: " + null);
        }
        else if(!cargoMap.containsKey(itemName)){
            throw new NoSuchElementException("Item doesnt exists in cargo map");
        }

        Item item = cargoMap.get(itemName);

        if (item.getAmount() + amount >= 0){
            item.updateAmount(amount);
        }
        else{
            throw new IllegalArgumentException("Invalid Amount, amount cant make cargo amount negative| amount: " + amount + ", item: " + item);
        }
    }

    public Item getItem(String itemName){
        if (itemName == null || !cargoMap.containsKey(itemName)){
            return null;
        }
        else{
            return cargoMap.get(itemName);
        }
    }

    @Override
    public String toString() {
        return "ShipCargo{" +
                "cargoMap=" + cargoMap +
                '}';
    }
}
