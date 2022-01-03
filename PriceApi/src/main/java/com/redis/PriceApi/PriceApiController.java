package com.redis.PriceApi;

import com.redis.dao.Item;
import com.redis.repository.ItemRepository;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/price")
public class PriceApiController {

    final Logger logger = LoggerFactory.getLogger(PriceApiController.class);

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/{item}")
    double oneItem(@PathVariable("item") String itemName) {
        logger.info("Request for price of item : " + itemName);
        if(itemRepository.findById(itemName).isPresent()){
            logger.info("Item price found for : " + itemName);
            return itemRepository.findById(itemName).get().getPrice();
        }
        logger.warn("Price for item : " + itemName + " couldn't be found");
        return -1.0;
    }

    @GetMapping("/browse/all")
    Iterable<Item> all() {
        logger.info("Browse All| Request to return all");
        return itemRepository.findAll();
    }

    @GetMapping("/browse")
    Iterable<Item> page(@RequestParam() int page, @RequestParam() int max) {
        logger.info("Browse| Request for pages : " + page + " with max : " + max);
        Iterable<Item> itemIterable = itemRepository.findAll();
        List<Item> result = IterableUtils.toList(itemIterable);
        logger.debug("Browse| Result| Size: {}", result.size());
        if(result.size() > (page * max) && (page * max) >= 0){
            int offset = calcOffset(page, max, result.size());
            return result.subList((page * max), (page * max) + offset);
        }
        logger.warn("Browse| Requested pages out of scope");
        return null;
    }

    int calcOffset(int page, int max, int resultSize){
        int offset = max;

        if(max < 0){
            logger.warn("max value in request was < 0");
            offset = 0;
        }

        if(((page * max) + max) > resultSize){
            offset = resultSize - (page * max);
        }

        return offset;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void addItem(@RequestBody Item item){
        logger.info("Adding new item : " + item.getName());
        itemRepository.save(item);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateItem(@RequestBody Item item){
        Optional<Item> amount = itemRepository.findById(item.getName());
        if(amount.isPresent()){
            double newPrice = amount.get().getPrice() + item.getPrice();
            item.setPrice(newPrice);
            if(item.getPrice() > 0){
                logger.info("Update item : " + item.getName());
                itemRepository.save(item);
            } else{
                logger.warn("Update to item : " + item.getName() + " failed as the update would cause amount to be less than 1");
            }
        }else{
            logger.warn("Item : " + item.getName() + " Couldn't be updated");
        }
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void removeItem(@RequestBody Item item){
        logger.info("Deleting item : " + item.getName());
        itemRepository.deleteById(item.getName());
    }
}
