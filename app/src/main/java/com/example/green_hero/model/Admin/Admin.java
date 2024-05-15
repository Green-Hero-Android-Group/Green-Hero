package com.example.green_hero.model.Admin;

import com.example.green_hero.model.User.Profile;

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;

public class Admin extends Profile {
    @PrimaryKey
    private ObjectId _id;
    private String name;
    private String email;
    private String password;
    private RealmList<Request> requests;

    public Admin() {
    }
    public Admin(ObjectId _id, String name, String email, String password) {
        super(_id, name, email, password);
        this.requests = new RealmList<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RealmList<Request> getRequests() {
        return requests;
    }

    public void setRequests(RealmList<Request> requests) {
        this.requests = requests;
    }
}
