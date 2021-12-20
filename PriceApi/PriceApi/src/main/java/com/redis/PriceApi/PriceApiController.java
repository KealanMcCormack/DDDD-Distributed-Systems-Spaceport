package com.redis.PriceApi;

import com.redis.dao.Item;
import com.redis.repository.ItemRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PriceApiController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/price/{item}")
    double oneItem(@PathVariable("item") String item) {
        if(itemRepository.findById(item).isPresent()){
            return itemRepository.findById(item).get().getPrice();
        }

        return -1.0;
    }

    @GetMapping("/price/browse/all")
    Iterable<Item> all() {
        return itemRepository.findAll();
    }

    @GetMapping("/price/browse")
    Iterable<Item> page(@RequestParam() int page, @RequestParam() int max) {
        Iterable<Item> itemIterable = itemRepository.findAll();
        List<Item> result = IterableUtils.toList(itemIterable);
        if(result.size() > (page * max) + max && (page * max) >= 0){
            return result.subList((page * max), (page * max) + max);
        }
        return null;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void addItem(@RequestBody Item item){
        itemRepository.save(item);
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void removeItem(@RequestBody Item item){
        itemRepository.deleteById(item.getName());
    }
}
