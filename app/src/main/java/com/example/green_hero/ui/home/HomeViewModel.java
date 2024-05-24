package com.example.green_hero.ui.home;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.green_hero.DB;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Trophy;
import com.example.green_hero.utils.Subscriptions;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class HomeViewModel extends ViewModel {

    private Realm realm = DB.realm;

    public void insertEntry() {
        Subscriptions.subscribeToClassicUser();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassicUser user = new ClassicUser("Dimitris", "email",
                        "1234", "user", 0, 0);
                Trophy trophy1 = new Trophy("Recycled 10 items", 10);
                Trophy trophy2 = new Trophy("Recycled 10 items", 10);
                Trophy trophy3 = new Trophy("Recycled 10 items", 10);
                RealmList<Trophy> trophies = new RealmList<>();
                trophies.add(trophy1);
                trophies.add(trophy2);
                trophies.add(trophy3);
                user.setTrophies(trophies);
                realm.insert(user);
                Log.v("QUICKSTART", "Successfully inserted user.");
            }
        });
    }

    public RealmResults<ClassicUser> getClassicUser() {
//        subscribeToClassicUser();
        RealmResults<ClassicUser> users = realm.where(ClassicUser.class).findAll();
        return users;

    }
}