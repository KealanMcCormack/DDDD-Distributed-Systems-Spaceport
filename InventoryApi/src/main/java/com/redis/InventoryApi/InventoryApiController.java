package com.redis.InventoryApi;

import com.redis.dao.Item;
import com.redis.repository.ItemRepository;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryApiController {

    final Logger logger = LoggerFactory.getLogger(InventoryApiController.class);

    @Autowired
    ItemRepository itemRepository;

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

    @GetMapping("/items")
    Iterable<Item> all() {
        logger.info("Request to return all");
        return itemRepository.findAll();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void addItem(@RequestBody Item item){
        logger.info("Adding new item : " + item.getName());
        itemRepository.save(item);
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void removeItem(@RequestBody Item item){
        logger.info("Deleting item : " + item.getName());
        itemRepository.deleteById(item.getName());
    }
}
