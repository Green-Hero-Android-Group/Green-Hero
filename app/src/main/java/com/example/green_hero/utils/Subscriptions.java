package com.example.green_hero.utils;

import android.util.Log;

import com.example.green_hero.DB;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Level;
import com.example.green_hero.model.User.Trophy;

import io.realm.Realm;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SubscriptionSet;

public class Subscriptions {
    private static Realm realm = DB.realm;
    public static void subscribeToClassicUser() {
        if (realm == null) {
            Log.e("QUICKSTART", "Realm is null.");
            return;
        }

        //Subscribing to the ClassicUser collection
        realm.getSubscriptions().update(new SubscriptionSet.UpdateCallback() {
            @Override
            public void update(MutableSubscriptionSet subscriptions) {
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(ClassicUser.class)
                        )
                );
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(Trophy.class)
                        )
                );
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(Level.class)
                        )
                );

            }
        });
    }

    public static void subscribeToTrophy() {
        if (realm == null) {
            Log.e("QUICKSTART", "Realm is null.");
            return;
        }

        //Subscribing to the ClassicUser collection
        realm.getSubscriptions().update(new SubscriptionSet.UpdateCallback() {
            @Override
            public void update(MutableSubscriptionSet subscriptions) {
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(Trophy.class)
                        )
                );
            }
        });
    }
}
