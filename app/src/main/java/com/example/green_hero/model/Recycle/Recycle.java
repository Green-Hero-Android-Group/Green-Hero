package com.example.green_hero.model.Recycle;

import io.realm.RealmObject;

public class Recycle extends RealmObject {
    private String date;
    private Item item;

    public Recycle() {
    }

    public Recycle(String date, Item item) {
        this.date = date;
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
