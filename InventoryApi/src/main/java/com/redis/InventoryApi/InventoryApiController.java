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
public class InventoryApiController {

    final Logger logger = LoggerFactory.getLogger(InventoryApiController.class);

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/inventory/{item}")
    double oneItem(@PathVariable("item") String itemName) {
        logger.info("Request for item : " + itemName);
        if(itemRepository.findById(itemName).isPresent()){
            logger.info("Item amount found for : " + itemName);
            return itemRepository.findById(itemName).get().getAmount();
        }
        logger.warn("Amount of item : " + itemName + " couldn't be found");
        return -1.0;
    }

    @GetMapping("/inventory/items")
    Iterable<Item> all() {
        logger.info("Request to return all");
        return itemRepository.findAll();
    }

    @GetMapping("/price/browse")
    Iterable<Item> page(@RequestParam() int page, @RequestParam() int max) {
        logger.info("Request for pages : " + page + " with max : " + max);
        Iterable<Item> itemIterable = itemRepository.findAll();
        List<Item> result = IterableUtils.toList(itemIterable);
        if(result.size() > (page * max) + max && (page * max) >= 0){
            return result.subList((page * max), (page * max) + max);
        }
        logger.warn("Requested pages out of scope");
        return null;
    }

    @PostMapping("/inventory/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void addItem(@RequestBody Item item){
        logger.info("Adding new item : " + item.getName());
        itemRepository.save(item);
    }

    @PostMapping("/inventory/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void removeItem(@RequestBody Item item){
        logger.info("Deleting item : " + item.getName());
        itemRepository.deleteById(item.getName());
    }
}
