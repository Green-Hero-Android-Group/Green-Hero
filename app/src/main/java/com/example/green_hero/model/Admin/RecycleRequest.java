package com.example.green_hero.model.Admin;

import com.example.green_hero.model.Recycle.Item;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RecycleRequest extends RealmObject {
    @PrimaryKey
    private ObjectId _id;
    private String date;
    private Item item;

    public RecycleRequest() {
    }

    public RecycleRequest(String date, Item item) {
        this._id = new ObjectId();
        this.date = date;
        this.item = item;
    }

    public RecycleRequest(ObjectId _id, String date, Item item) {
        this._id = _id;
        this.date = date;
        this.item = item;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
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
