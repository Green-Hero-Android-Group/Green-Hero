package com.example.green_hero.model.User;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Level extends RealmObject {
    @PrimaryKey
    private ObjectId _id;
    private int level;
    private int xp;

    public Level(){

    }

    public Level (int level, int xp) {
        this._id = new ObjectId();
        this.level = level;
        this.xp = xp;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
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

    public int xpToNextLevel(ClassicUser user){
        return xp-user.getXp();

    }
}
