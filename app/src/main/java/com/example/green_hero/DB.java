
package com.example.green_hero;

import android.app.Application;
import android.util.Log;

import com.example.green_hero.model.Recycle.Item;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Level;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmResults;
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
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        AppConfiguration appConfig = new AppConfiguration.Builder(appID).build();
        app = new App(appConfig);
    }

    public static User loginSync(Credentials credentials) {

        app.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated.");
            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: " + it.getError().getErrorMessage());
            }
        });
        return app.currentUser();
    }

    public static User signUpSync(String name,String email, String password) {
        app.getEmailPassword().registerUserAsync(email, password, it -> {
            if (it.isSuccess()) {
                Log.v("QUICKSTART", "Successfully registered user.");
            } else {
                Log.e("QUICKSTART", "Failed to register user: " + it.getError().getErrorMessage());
            }
            Credentials credentials = Credentials.emailPassword(email, password);
            app.loginAsync(credentials, it2 -> {
                if (it2.isSuccess()) {
                    Log.v("QUICKSTART", "Successfully authenticated.");
                } else {
                    Log.e("QUICKSTART", "Failed to log in. Error: " + it2.getError().getErrorMessage());
                }
                User loggedInUser = app.currentUser();
                    SyncConfiguration.InitialFlexibleSyncSubscriptions handler = new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                        @Override
                        public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                            subscriptions.addOrUpdate(
                                    Subscription.create(
                                            realm.where(ClassicUser.class)
                                    )
                            );
                            subscriptions.addOrUpdate(
                                    Subscription.create(
                                            realm.where(Level.class)
                                    )
                            );
                        }
                    };

                    SyncConfiguration flexibleSyncConfig = new SyncConfiguration.Builder(loggedInUser)
                            .initialSubscriptions(handler)
                            .allowQueriesOnUiThread(true)
                            .allowWritesOnUiThread(true)
                            .build();

                    realm = Realm.getInstance(flexibleSyncConfig);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            ClassicUser user = new ClassicUser(name, email,
                                    password, "user", new Level(0, 0), 0);
                            realm.insert(user);
                            Log.v("QUICKSTART", "Successfully inserted user.");
                        }
                    });
                    System.out.println("Successfully signed up as: " + loggedInUser.isLoggedIn());
                });
            });
        return app.currentUser();
    }

    public static ClassicUser getClassicUser() {
//        subscribeToClassicUser();
        RealmResults<ClassicUser> users = realm.where(ClassicUser.class).findAll();
        for(ClassicUser user : users) {
            if(user.getEmail().equals(DB.app.currentUser().getProfile().getEmail())) {
                return user;
            }
        }
        return null;
    }

    static void initializeRealm(User user) {
        SyncConfiguration.InitialFlexibleSyncSubscriptions handler = new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
            @Override
            public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(ClassicUser.class)
                        )
                );
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(Level.class)
                        )
                );
            }
        };

        SyncConfiguration flexibleSyncConfig = new SyncConfiguration.Builder(user)
                .initialSubscriptions(handler)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();

        realm = Realm.getInstance(flexibleSyncConfig);
    }
}
