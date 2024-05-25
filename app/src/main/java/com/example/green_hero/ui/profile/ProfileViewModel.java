package com.example.green_hero.ui.profile;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.green_hero.DB;
import com.example.green_hero.model.User.ClassicUser;

import io.realm.Realm;

public class ProfileViewModel extends ViewModel{
    private Realm realm = DB.realm;

    public void changeName(String name) {
        if (realm == null) {
            Log.e("QUICKSTART", "Realm is null. Did you forget to call DB.init()?");
            return;
        }
        ClassicUser user = DB.getClassicUser();
        realm.executeTransaction(r -> {
            user.setName(name);
        });
    }
}
