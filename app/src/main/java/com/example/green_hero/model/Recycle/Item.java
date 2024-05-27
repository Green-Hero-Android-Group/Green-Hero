package com.example.green_hero.model.Recycle;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {
    @PrimaryKey
    private ObjectId _id;
    private String name;
    private int quantity;
    private long longQuantity;
    private String type;

    public Item() {
    }

    public Item(String name, int quantity, String type) {
        this._id = new ObjectId();
        this.name = name;
        this.quantity = quantity;
        this.type = type;
    }

    public Item(ObjectId _id, String name, int quantity, String type) {
        this._id = _id;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
    }

    public Item(ObjectId _id, String name, long quantity, String type) {
        this._id = _id;
        this.name = name;
        this.longQuantity = quantity;
        this.type = type;
    }

    public long getLongQuantity() {
        return longQuantity;
    }

    public void setLongQuantity(long longQuantity) {
        this.longQuantity = longQuantity;
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
