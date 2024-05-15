package com.example.green_hero.model.User;


import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Profile extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private String name;
    private String password;
    private String email;

    public Profile(ObjectId _id, String name, String password, String email) {
        this._id = _id;
        this.name=name;
        this.password=password;
        this.email=email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile(){

    }
}
