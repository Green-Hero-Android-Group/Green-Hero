package com.example.green_hero.model.Recycle;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {
    @PrimaryKey
    private ObjectId _id;
    private String name;
    private int quantity;
    private String type;

    public Item() {
    }

    public Item(ObjectId _id, String name, int quantity, String type) {
        this._id = _id;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
