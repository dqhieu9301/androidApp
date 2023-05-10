package com.example.appandroid.model;

public class ProductHistory {
    private int id;
    private String name;
    private int quantity;
    private int cost;
    private String updated_at;
    private String path;

    public ProductHistory() {

    }

    public ProductHistory(int id, String name, int quantity, int cost, String updated_at, String path) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.updated_at = updated_at;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
