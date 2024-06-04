package com.example.green_hero;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.green_hero.model.Admin.RecycleRequest;
import com.example.green_hero.model.Admin.Request;
import com.example.green_hero.model.Recycle.Item;
import com.example.green_hero.model.Recycle.Recycle;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.ui.manage.ManageFragment;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.RealmList;

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
                RealmList<RecycleRequest> recycleRequests = DB.recycleRequests;

                for(RecycleRequest recycleRequest : recycleRequests){
                    if(recycleRequest.get_id().equals(request.getRecycle().get_id())){
                        System.out.println("Admin Approval:" + recycleRequest.get_id());
                        System.out.println(recycleRequest.getDate());
                        System.out.println(recycleRequest.getItem().getName());
                        recycle.set_id(new ObjectId());
                        recycle.setDate(recycleRequest.getDate());
                        recycle.setItem(item);
                        user.getRecycles().add(recycle);
                        realm.copyToRealmOrUpdate(user);
                    }
                }
        });
    }
}
