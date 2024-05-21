package com.example.green_hero.model.User;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Trophy extends RealmObject {
    @PrimaryKey
    private ObjectId _id;
    private String name;
    private int index;


    public Trophy(String name, int index) {
        this._id = new ObjectId();
        this.name = name;
        this.index=index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Trophy() {
    }
}
