package com.buysell.market.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
/**
 * Class representing an Item on the Market.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item implements Serializable {
    private String name;
    private double amount;

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
