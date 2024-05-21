package com.example.green_hero.ui.profile;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.green_hero.DB;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Level;
import com.example.green_hero.model.User.Trophy;
import com.example.green_hero.utils.Subscriptions;

import io.realm.Realm;
import io.realm.RealmList;

public class ProfileViewModel extends ViewModel{
    private Realm realm = DB.realm;

    public void insertEntry() {
        if (realm == null) {
            Log.e("QUICKSTART", "Realm is null. Did you forget to call DB.init()?");
            return;
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassicUser user = new ClassicUser("Dimitris", "dimsparagis@gmail.com",
                        "1234", "user", null, 0);
                realm.insert(user);
                Log.v("QUICKSTART", "Successfully inserted user.");
            }
        });
    }

    public ClassicUser getClassicUser() {
        ClassicUser user = realm.where(ClassicUser.class).equalTo("name", "Dimitris").findFirst();
        return user;
    }

    public void insertSampleEntries() {
        Subscriptions.subscribeToClassicUser();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassicUser user = new ClassicUser("Dimitris", "dimsparagis@gmail.com",
                        "123456", "user", new Level(0, 100), 0);
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

}
