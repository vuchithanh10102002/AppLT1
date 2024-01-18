package com.example.applt.model;

public class Product {
    private int id;
    private String name;
    private int price;
    private int price_old;
    private String description, content;
    private String image;
    private Product() {};

    public Product(String description, String content, int id ,String image , String name, int price, int price_old) {
        this.name = name;
        this.price = price;
        this.price_old = price_old;
        this.description = description;
        this.content = content;
        this.id = id;
        this.image = image;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice_old() {
        return price_old;
    }

    public void setPrice_old(int price_old) {
        this.price_old = price_old;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
