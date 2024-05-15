package com.example.green_hero.model.User;

public class Level {

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
