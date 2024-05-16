package com.example.green_hero.model.User;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Level extends RealmObject {
    @PrimaryKey
    private ObjectId _id;
    private int xp;

    public Level(){

    }
    public Level(int xp){
        this.xp=xp;
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
