package com.example.green_hero.model.User;


import com.example.green_hero.model.Recycle.Recycle;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class ClassicUser extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private String name;
    private String email;
    private String password;
    private String role;
    private int level;
    private int xp;
    private RealmList<Trophy> trophies;
    private RealmList<Recycle> recycles;

    public ClassicUser(String name,String email,String password,String role, int level, int xp) {
        this.name = name;
        this.email = email;
        this.password = password;
        this._id = new ObjectId();
        this.role = role;
        this.level = level;
        this.xp = xp;
        this.trophies = new RealmList<Trophy>();
        this.recycles = new RealmList<Recycle>();
    }

    public ClassicUser(ObjectId _id,String name,String email,String password,String role, int level,
                       int xp, RealmList<Trophy> trophies, RealmList<Recycle> recycles) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.level = level;
        this.xp = xp;
        this.trophies =  trophies;
        this.recycles =  recycles;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public RealmList<Trophy> getTrophies() {
        return trophies;
    }

    public void setTrophies(RealmList<Trophy> trophies) {
        this.trophies = trophies;
    }

    public RealmList<Recycle> getRecycles() {
        return recycles;
    }

    public void setRecycles(RealmList<Recycle> recycles) {
        this.recycles = recycles;
    }

    public ClassicUser() {
    }
}
