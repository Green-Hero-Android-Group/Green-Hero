package com.example.green_hero.utils;

import com.example.green_hero.DB;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Trophy;

import io.realm.Realm;
import io.realm.mongodb.User;

public class Transactions {
    static Realm realm = DB.realm;

    public static void updateUserTrophies(Trophy trophy) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassicUser user = DB.getClassicUser();
                user.getTrophies().add(trophy);
                realm.copyToRealmOrUpdate(user);
            }
        });
    }
}
