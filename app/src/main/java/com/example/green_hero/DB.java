
package com.example.green_hero;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Collectible;
import com.example.green_hero.model.User.Trophy;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.FindIterable;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class DB extends Application {
    private static String appID = "application-0-rexuosx";
    public static Realm realm;
    public static App app;
    public static ArrayList<Trophy> trophies = new ArrayList<>();
    public static ArrayList<Collectible> rewards = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.removeDefaultConfiguration();
        AppConfiguration appConfig = new AppConfiguration.Builder(appID).build();
        app = new App(appConfig);
        trophies.add(new Trophy("Welcome Hero", 1));
        trophies.add(new Trophy("Reached Level 2", 2));
        trophies.add(new Trophy("Reached Level 3", 3));
        trophies.add(new Trophy("Reached Level 4", 4));
        trophies.add(new Trophy("Reached Level 5", 5));
        trophies.add(new Trophy("Reached Level 6", 6));
        trophies.add(new Trophy("Reached Level 7", 7));
        trophies.add(new Trophy("Reached Level 8", 8));
        trophies.add(new Trophy("Reached Level 9", 9));
        trophies.add(new Trophy("Reached Level 10", 10));
        trophies.add(new Trophy("Recycled 1 item", 1));
        trophies.add(new Trophy("Recycled 2 items", 2));
        trophies.add(new Trophy("Recycled 3 items", 3));
        trophies.add(new Trophy("Recycled 4 items", 4));
        trophies.add(new Trophy("Recycled 5 items", 5));

        rewards.add(new Collectible("Recycling Rookie", 1));
        rewards.add(new Collectible("Eco-Warrior", 2));
        rewards.add(new Collectible("Paper Badge", 3));
        rewards.add(new Collectible("Glass Badge", 4));
    }

    public static void onCreateAfterLogOut() {
        Realm.removeDefaultConfiguration();
        AppConfiguration appConfig = new AppConfiguration.Builder(appID).build();
        app = new App(appConfig);
    }

    public static User loginSync(Credentials credentials, OnUserLoginCallback callback) {
        app.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated.");
                callback.onUserLoggedIn(app.currentUser());
            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: " + it.getError().getErrorMessage());
                callback.onUserLoggedIn(null);
            }
//            getTrophies();
        });
        return app.currentUser();
    }

    public interface OnUserLoginCallback {
        void onUserLoggedIn(User user);
    }


    public static User signUpSync(String name, String email, String password, Context c,OnUserLoginCallback callback) {
        Credentials credentials1 = Credentials.emailPassword(email, password);
        app.loginAsync(credentials1, it -> {
            if (it.isSuccess()) {
                Toast.makeText(c, "User already exists", Toast.LENGTH_SHORT).show();
            } else {
                app.getEmailPassword().registerUserAsync(email, password, it2 -> {
                    if (it2.isSuccess()) {
                        Log.v("QUICKSTART", "Successfully registered user.");
                    } else {
                        Log.e("QUICKSTART", "Failed to register user: " + it2.getError().getErrorMessage());
                    }
                    Credentials credentials = Credentials.emailPassword(email, password);
                    app.loginAsync(credentials, it3 -> {
                        if (it3.isSuccess()) {
                            Log.v("QUICKSTART", "Successfully authenticated.");
                        } else {
                            Log.e("QUICKSTART", "Failed to log in. Error: " + it3.getError().getErrorMessage());
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
                                                realm.where(Trophy.class)
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
                                        password, "user", 1, 0);
                                realm.insert(user);
                                Log.v("QUICKSTART", "Successfully inserted user.");
                            }
                        });
                        System.out.println("Successfully signed up as: " + loggedInUser.isLoggedIn());
                        callback.onUserLoggedIn(loggedInUser);
                    });
                });
            }
        });

        return app.currentUser();
    }


    public static ClassicUser getClassicUser() {
//        subscribeToClassicUser();
        RealmResults<ClassicUser> users = realm.where(ClassicUser.class).findAll();
        for (ClassicUser user : users) {
            if (user.getEmail().equals(DB.app.currentUser().getProfile().getEmail())) {
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
                                realm.where(Trophy.class)
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
    public interface OnGetTrophiesCallback {
        void OnGetTrophies();
    }

    public static void getTrophies(OnGetTrophiesCallback callback) {
        User user = app.currentUser();
        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase =
                mongoClient.getDatabase("todo");
        // registry to handle POJOs (Plain Old Java Objects)
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Trophy"); // Replace with your actual collection name

        FindIterable<Document> allDocuments = mongoCollection.find();
        RealmResultTask<MongoCursor<Document>> items = allDocuments.iterator();
        items.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                while (results.hasNext()) {
                    Document trophy = results.next();
                    trophies.add(new Trophy(trophy.getString("name"), trophy.getInteger("index")));
                }
                for (Trophy trophy : trophies) {
                    Log.v("QUICKSTART", "Trophy: " + trophy.getName());
                }
            } else {
                Log.e("QUICKSTART", "failed to find documents with: ", task.getError());
            }
            callback.OnGetTrophies();
        });
    }
}
