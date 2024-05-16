package com.example.green_hero.model.User;

import org.bson.types.ObjectId;

import io.realm.annotations.PrimaryKey;

public class Collectible {
    @PrimaryKey
    private ObjectId _id;
    private String name;
    private int index;

    public Collectible(String name, int index) {
        this.name=name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Collectible() {
    }
}
