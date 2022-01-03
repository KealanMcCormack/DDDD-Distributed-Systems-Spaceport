package client.items;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private double price;
    private double amount;

    public Item(){
        this.name = "";
        this.price = -1;
        this.amount = -1;
    }

    public Item(String name){
        this.name = name;
        this.price = -1;
        this.amount = -1;
    }

    public Item(String name, double price){
        this.name = name;
        this.price = price;
        this.amount = -1;
    }

    public Item(String name, double price, double amount){
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Item){
            Item itemO = (Item) o;

            return this.name.equals(itemO.name);
        }

        return super.equals(o);
    }

    public void updateAmount(double amount){
        this.amount += amount;
    }

}


