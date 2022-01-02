package com.redis.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * Class representing an item in the Inventory
 */
@RedisHash("Item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item implements Serializable {
    @Id private String name;
    private double amount;

}
