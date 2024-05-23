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
}
