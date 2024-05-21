package com.example.green_hero.ui.recycle;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.green_hero.DB;
import com.example.green_hero.model.Recycle.Item;
import com.example.green_hero.model.Recycle.Recycle;
import com.example.green_hero.model.User.ClassicUser;

import io.realm.Realm;

public class RecycleViewModel extends ViewModel {
    private Realm realm = DB.realm;

    public void insertEntry() {
        if (realm == null) {
            Log.e("QUICKSTART", "Realm is null. Did you forget to call DB.init()?");
            return;
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassicUser user = new ClassicUser("Dimitris4", "dimsparagis@gmail.com",
                        "1234", "user", null, 0);
                realm.insert(user);
                Log.v("QUICKSTART", "Successfully inserted user.");
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item newItem = new Item("News Paper", 2, "Paper");
                realm.insert(newItem);
                Log.v("QUICKSTART", "Successfully inserted newItem.");
            }
        });
    }
}
