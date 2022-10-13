package com.techelevator;

public class Item {

    private String finalThought;
    private String name;
    private double price;
    private String location;
    private String category;
    private int quantity = 5;

    public Item(String name, double price, String location, String category, String finalThought) {
        this.name = name;
        this.price = price;
        this.location = location;
        this.category = category;
        this.finalThought = finalThought;
        this.quantity = quantity;

    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getFinalThought() {
        return finalThought;
    }

    public void printThisItem() {
        System.out.println(this.location + " " + this.name + " " + this.price + " " + this.category + " " + this.quantity);
    }

    public void deduct() {
        quantity--;
    }
}


