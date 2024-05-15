package com.example.green_hero.model.User;

import org.bson.types.ObjectId;

import io.realm.RealmList;

public class ClassicUser extends Profile{

    private String role;
    private Level level;
    private int xp;
    private RealmList<Trophy> trophies;

    public ClassicUser(ObjectId _id, String name, String password, String email, int xp) {
        super(_id, name, password, email);
        this.xp = xp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public ClassicUser() {
    }
}
