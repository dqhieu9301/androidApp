package com.example.appandroid.model;

public class ProductHistory {
    private String name;
    private int quantity;
    private String date;
    private String path;

    public ProductHistory() {

    }

    public ProductHistory(String name, int quantity, String date, String path) {
        this.name = name;
        this.quantity = quantity;
        this.date = date;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
