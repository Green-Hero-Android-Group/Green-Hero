package com.example.green_hero.model.User;

public class Collectible {
    private String name;
    private int index;

    public Collectible(String name, int index) {
        this.name=name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Collectible() {
    }
}
