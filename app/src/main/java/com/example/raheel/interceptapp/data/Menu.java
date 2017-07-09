package com.example.raheel.interceptapp.data;

import android.content.ContentValues;

/**
 * Created by Raheel on 6/27/2017.
 */

public class Menu {

    private String id;
    private String name;
    private Double price;
    private String type;

    public Menu(String id, String name, Double price, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public ContentValues getContentValues() {
//
//        ContentValues values = new ContentValues();
//        values.put("id", id);
//        values.put("name", name);
//        values.put("price", price);
//        values.put("type", type);
//
//        return values;
//    }
}
