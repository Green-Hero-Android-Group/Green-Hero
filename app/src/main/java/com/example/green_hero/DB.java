package com.example.green_hero;

import android.app.Application;
import android.util.Log;

import com.example.green_hero.model.User.ClassicUser;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class DB extends Application {
    private String appID = "application-0-rexuosx";
    public static Realm realm;
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        AppConfiguration appConfig = new AppConfiguration.Builder(appID).build();
        app = new App(appConfig);
        User user = loginSync(Credentials.emailPassword("dimsparagis@gmail.com", "123456"));
        if (user != null) {
            initializeRealm(user);
        }
    }

    public static User loginSync(Credentials credentials) {


        app.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated anonymously.");
            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: " + it.getError().getErrorMessage());
            }
        });
        return app.currentUser();
    }

    private static void initializeRealm(User user) {
        SyncConfiguration.InitialFlexibleSyncSubscriptions handler = new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
            @Override
            public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(ClassicUser.class)
                        )
                );
            }
        };

        SyncConfiguration flexibleSyncConfig = new SyncConfiguration.Builder(app.currentUser())
                .initialSubscriptions(handler).build();
        Realm realm = Realm.getInstance(flexibleSyncConfig);

        realm = Realm.getInstance(flexibleSyncConfig);
    }
}
