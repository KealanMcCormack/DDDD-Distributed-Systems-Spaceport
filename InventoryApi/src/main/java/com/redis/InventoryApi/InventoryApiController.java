package com.redis.InventoryApi;

import com.redis.dao.Item;
import com.redis.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Inventory API Controller used for representing the inventory of the space port.
 */
@RestController
@RequestMapping("/inventory")
public class InventoryApiController {

    final Logger logger = LoggerFactory.getLogger(InventoryApiController.class);

    @Autowired
    ItemRepository itemRepository;

    /**
     * Get Mapping for retrieving amount of given item.
     * @param itemName
     * @return double
     */
    @GetMapping("/{item}")
    double oneItem(@PathVariable("item") String itemName) {
        logger.info("Request for item : " + itemName);
        if(itemRepository.findById(itemName).isPresent()){
            logger.info("Item amount found for : " + itemName);
            return itemRepository.findById(itemName).get().getAmount();
        }
        logger.warn("Amount of item : " + itemName + " couldn't be found");
        return -1.0;
    }

    /**
     * Get Mapping for returning total list of items in inventory
     * @return itemRepository.findAll();
     */
    @GetMapping("/items")
    Iterable<Item> all() {
        logger.info("Request to return all");
        return itemRepository.findAll();
    }

    /**
     * Post Mapping for adding given item to the inventory.
     * @param item
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void addItem(@RequestBody Item item){
        logger.info("Adding new item : " + item.getName());
        itemRepository.save(item);
    }

    /**
     * Post Mapping for updating the price or amount of an already existing item.
     * @param item
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateItem(@RequestBody Item item){
        Optional<Item> amount = itemRepository.findById(item.getName());
        if(amount.isPresent()){
            double newAmount = amount.get().getAmount() + item.getAmount();
            item.setAmount(newAmount);
            if(item.getAmount() > 0){
                logger.info("Update item : " + item.getName());
                itemRepository.save(item);
            } else{
                logger.warn("Update to item : " + item.getName() + " failed as the update would cause amount to be less than 1");
            }

        }else{
            logger.warn("Item : " + item.getName() + " Couldn't be updated");
        }


    }

    /**
     * Post Mapping for deleting given item from the inventory.
     * @param item
     */
    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void removeItem(@RequestBody Item item){
        logger.info("Deleting item : " + item.getName());
        itemRepository.deleteById(item.getName());
    }
}
