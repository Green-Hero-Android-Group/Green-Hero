package com.example.green_hero.ui.logIn;

import static com.example.green_hero.DB.realm;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.green_hero.AppActivity;
import com.example.green_hero.AuthActivity;
import com.example.green_hero.DB;
import com.example.green_hero.model.User.ClassicUser;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoginViewModel extends ViewModel {
    private Realm realm = DB.realm;
    Class routeClass;

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



    public void login(EditText username, EditText password) {


        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Query the database to check if the user exists
                RealmResults<ClassicUser> users = realm.where(ClassicUser.class)
                        .equalTo("username", usernameString)
                        .equalTo("password", passwordString)
                        .findAll();
                if (!users.isEmpty()) {
                    Log.v("QUICKSTART", "Successfully logged in.");
                }
//                else {
//
//                }
            }
        });


    }


    public void login(String dimitris, String number) {
    }
}
