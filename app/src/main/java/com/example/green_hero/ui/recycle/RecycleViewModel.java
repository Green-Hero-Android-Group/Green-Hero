package com.example.green_hero.ui.recycle;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.green_hero.DB;
import com.example.green_hero.model.Admin.RecycleRequest;
import com.example.green_hero.model.Admin.Request;
import com.example.green_hero.model.Recycle.Item;
import com.example.green_hero.model.Recycle.Recycle;
import com.example.green_hero.model.User.ClassicUser;

import java.time.LocalDate;

import io.realm.Realm;

public class RecycleViewModel extends ViewModel {
    private Realm realm = DB.realm;

    public void insertItem(Item aItem) {
        if (realm == null) {
            Log.e("QUICKSTART", "Realm is null. Did you forget to call DB.init()?");
            return;
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item newItem = new Item(aItem.getName(), aItem.getQuantity(), aItem.getType()); //future refactor -> I can just do realm.insert(aItem);
                LocalDate date = LocalDate.now();
                realm.insert(newItem);
                Log.v("QUICKSTART", "Successfully inserted new Item.");
                RecycleRequest recycleReq = new RecycleRequest(date.toString(), newItem);
                realm.insert(recycleReq);
//                Request request = new Request(user, false, recycleReq);
//                realm.insert(request);
            }
        });
    }
}
