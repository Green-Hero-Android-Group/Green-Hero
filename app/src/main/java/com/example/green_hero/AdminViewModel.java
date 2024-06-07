package com.example.green_hero;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.green_hero.model.Admin.RecycleRequest;
import com.example.green_hero.model.Admin.Request;
import com.example.green_hero.model.Recycle.Item;
import com.example.green_hero.model.Recycle.Recycle;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Trophy;
import com.example.green_hero.ui.manage.ManageFragment;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AdminViewModel extends ViewModel {
    private Realm realm = DB.realm;

    public void approveRequest(Item item, ClassicUser user, Request request) {
        if (realm == null) {
            Log.e("QUICKSTART", "Realm is null. Did you forget to call DB.init()?");
            return;
        }

        Recycle recycle = new Recycle();
        realm.executeTransaction(realmTrans -> {
            //Clearing the status of the request to true
            request.setStatus(true);
            realm.copyToRealmOrUpdate(request);

            //Creating a new Recycle object
            RealmResults<RecycleRequest> recycleRequests = DB.realm.where(RecycleRequest.class).findAll();

            for (RecycleRequest recycleRequest : recycleRequests) {
                if (recycleRequest.get_id().equals(request.getRecycle().get_id())) {
                    System.out.println("Admin Approval:" + recycleRequest.get_id());
                    System.out.println(recycleRequest.getDate());
                    System.out.println(recycleRequest.getItem().getName());
                    recycle.set_id(new ObjectId());
                    recycle.setDate(recycleRequest.getDate());
                    recycle.setItem(item);
                    user.getRecycles().add(recycle);
                    for (Trophy trophy : DB.trophies) {
                        if (trophy.getName().equals("Recycled " + user.getRecycles().size() + " Item")) {
                            user.getTrophies().add(trophy);
                        }
                        if (trophy.getName().equals("Recycled " + user.getRecycles().size() + " Items")) {
                            user.getTrophies().add(trophy);
                        }
                    }

                    //Updating the user's XP and leveling up if necessary
                    user.setXp(user.getXp() + 100 / user.getLevel());
                    if (user.getXp() == 100) {
                        user.setLevel(user.getLevel() + 1);
                        user.setXp(0);
                        for (Trophy trophy : DB.trophies) {
                            if (trophy.getName().equals("Reached Level " + user.getLevel())) {
                                user.getTrophies().add(trophy);
                            }
                        }
                    }

                    realm.copyToRealmOrUpdate(user);
                }
            }
        });
    }

    public void rejectRequest(Request request) {
        if (realm == null) {
            Log.e("QUICKSTART", "Realm is null. Did you forget to call DB.init()?");
            return;
        }

        realm.executeTransaction(realmTrans -> {
            //Clearing the status of the request to true
            request.setStatus(false);
            realm.copyToRealmOrUpdate(request);
        });
    }
}
