package client.shoppinglist;

import client.items.Item;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ShoppingList {

    public Map<String, Item> shoppingList;

    public ShoppingList(Map<String, Item> shoppingList){
        this.shoppingList = shoppingList;
    }

    public void updateListItem(double amount, String itemName) throws IllegalArgumentException{
        if (itemName == null){
            throw new IllegalArgumentException("Invalid Item name | name: " + null);
        }

        if (shoppingList.containsKey(itemName)){
            shoppingList.get(itemName).updateAmount(amount);
        }
        else{
            throw new IllegalArgumentException("Invalid Item name, item not in List| name: " + itemName);
        }
    }

    public Item removeListItem(String itemName) throws IllegalArgumentException{
        if (itemName == null){
            throw new IllegalArgumentException("Invalid Item name| name: " + null);
        }

        if (shoppingList.containsKey(itemName)){
            return shoppingList.remove(itemName);
        }
        else{
            throw new IllegalArgumentException("Invalid Item name, item not in List| name: " + itemName);
        }
    }

    public boolean listComplete(){
        return shoppingList == null || shoppingList.size() == 0;
    }

    public void removeCompletedItems(){
        shoppingList.entrySet()
                .removeIf(
                        entry -> (0 == entry.getValue().getAmount()));
    }

    public Collection<Item> getDesiredItems(){
        return shoppingList.values();
    }

    public Iterator<Map.Entry<String, Item>> getDesiredItemsIterator(){
        return shoppingList.entrySet().iterator();
    }
}
