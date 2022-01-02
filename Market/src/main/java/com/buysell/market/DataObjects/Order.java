package com.buysell.market.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class representing an Order.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    private String name;
    private int amount;
    private double totalCost;
}
