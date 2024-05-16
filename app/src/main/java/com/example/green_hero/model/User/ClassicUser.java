package com.example.green_hero.model.User;


import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ClassicUser extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private String role;
    private Level level;
    private int xp;
    private RealmList<Trophy> trophies;

    public ClassicUser(String role, Level level, int xp, RealmList<Trophy> trophies) {
        this.role = role;
        this.level = level;
        this.xp = xp;
        this.trophies = trophies;
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
