package com.example.appandroid.model;

public class Product {
    private String nameProduct, type, pathImage;
    private int cost, quantity;

    public Product() {}

    public Product(String nameProduct, String type, String pathImage, int cost, int quantity) {
        this.nameProduct = nameProduct;
        this.type = type;
        this.pathImage = pathImage;
        this.cost = cost;
        this.quantity = quantity;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
