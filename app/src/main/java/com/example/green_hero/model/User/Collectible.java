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

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Collectible() {
    }
}
