
package com.example.green_hero;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.green_hero.model.Admin.RecycleRequest;
import com.example.green_hero.model.Admin.Request;
import com.example.green_hero.model.Recycle.Item;
import com.example.green_hero.model.Recycle.Recycle;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Collectible;
import com.example.green_hero.model.User.Trophy;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
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
    public static RealmList<ClassicUser> users;
    public static RealmList<RecycleRequest> recycleRequests;
    public static RealmList<Request> requests;
    public static RealmList<Item> items;
    public static RealmList<Recycle> recycles;
    public static RealmList<Trophy> trophiesAll;

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

    public static User googleSignInSync(String name, String email, String password, Context c, OnUserLoginCallback callback) {
        Credentials credentials1 = Credentials.emailPassword(email, password);
        app.loginAsync(credentials1, it3 -> {
            if (it3.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated Google.");
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
        return app.currentUser();
    }



    public static User signUpSync(String name, String email, String password, Context c, OnUserLoginCallback callback) {
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
                                subscriptions.addOrUpdate(
                                        Subscription.create(
                                                realm.where(Item.class)
                                        )
                                );
                                subscriptions.addOrUpdate(
                                        Subscription.create(
                                                realm.where(Recycle.class)
                                        )
                                );
                                subscriptions.addOrUpdate(
                                        Subscription.create(
                                                realm.where(RecycleRequest.class)
                                        )
                                );
                                subscriptions.addOrUpdate(
                                        Subscription.create(
                                                realm.where(Request.class)
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
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(Item.class)
                        )
                );
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(Recycle.class)
                        )
                );
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(RecycleRequest.class)
                        )
                );
                subscriptions.addOrUpdate(
                        Subscription.create(
                                realm.where(Request.class)
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


    public static void getRequests(OnGetDataCallback callback) {
        User user = app.currentUser();
        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase =
                mongoClient.getDatabase("todo");
        // registry to handle POJOs (Plain Old Java Objects)
        MongoCollection<Document> classicUserMongoCollection = mongoDatabase.getCollection("ClassicUser");
        MongoCollection<Document> recycleRequestCollection = mongoDatabase.getCollection("RecycleRequest");
        MongoCollection<Document> requestCollection = mongoDatabase.getCollection("Request");
        MongoCollection<Document> trophyMongoCollection = mongoDatabase.getCollection("Trophy");
        MongoCollection<Document> recycleMongoCollection = mongoDatabase.getCollection("Recycle");

        users = new RealmList<>();
        recycleRequests = new RealmList<>();
        requests = new RealmList<>();
        items = new RealmList<>();
        recycles = new RealmList<>();
        trophiesAll = new RealmList<>();


        FindIterable<Document> allUsers = classicUserMongoCollection.find();
        FindIterable<Document> allRecycleRequests = recycleRequestCollection.find();
        FindIterable<Document> allRequests = requestCollection.find();
        FindIterable<Document> allItems = mongoDatabase.getCollection("Item").find();
        FindIterable<Document> allRecycles = recycleMongoCollection.find();
        FindIterable<Document> allTrophies = trophyMongoCollection.find();

        RealmResultTask<MongoCursor<Document>> userItems = allUsers.iterator();
        RealmResultTask<MongoCursor<Document>> recycleRequestItems = allRecycleRequests.iterator();
        RealmResultTask<MongoCursor<Document>> requestItems = allRequests.iterator();
        RealmResultTask<MongoCursor<Document>> itemItems = allItems.iterator();
        RealmResultTask<MongoCursor<Document>> recycleItems = allRecycles.iterator();
        RealmResultTask<MongoCursor<Document>> trophyItems = allTrophies.iterator();

        //Fetch all users
        trophyItems.getAsync(task5 -> {
            if (task5.isSuccess()) {
                MongoCursor<Document> results = task5.get();
                while (results.hasNext()) {
                    Document resultTrophy = results.next();
                    trophiesAll.add(new Trophy(resultTrophy.getObjectId("_id"),
                            resultTrophy.getString("name"),
                            Math.toIntExact(resultTrophy.getLong("index"))));
                }
                for (Trophy t : trophiesAll) {
                    Log.v("QUICKSTART", "Trophy: " + t.get_id());
                }
            } else {
                Log.e("QUICKSTART", "failed to find trophies with: ", task5.getError());
            }
            itemItems.getAsync(task3 -> {
                if (task3.isSuccess()) {
                    MongoCursor<Document> results = task3.get();
                    while (results.hasNext()) {
                        Document resultItem = results.next();
                        items.add(new Item(resultItem.getObjectId("_id"),
                                resultItem.getString("name"),
                                Math.toIntExact(resultItem.getLong("quantity")),
                                resultItem.getString("type")));
                    }
                    for (Item r : items) {
                        Log.v("QUICKSTART", "Item: " + r.get_id());
                    }
                } else {
                    Log.e("QUICKSTART", "failed to find items with: ", task3.getError());
                }
                recycleItems.getAsync(task4 -> {
                    if (task4.isSuccess()) {
                        MongoCursor<Document> results = task4.get();
                        RealmList<Item> recycleItemsAll = new RealmList<>();
                        while (results.hasNext()) {
                            Document resultRecycle = results.next();
                            for (Item items : items) {
                                if (items.get_id().equals(resultRecycle.getObjectId("item"))) {
                                    recycles.add(new Recycle(resultRecycle.getObjectId("_id"),
                                            resultRecycle.getString("date"),
                                            items));
                                }
                            }
                        }
                        for (Recycle r : recycles) {
                            Log.v("QUICKSTART", "Recycle: " + r.getItem().getName());
                        }
                    } else {
                        Log.e("QUICKSTART", "failed to find recycles with: ", task4.getError());
                    }
                    userItems.getAsync(task -> {
                        if (task.isSuccess()) {
                            MongoCursor<Document> results = task.get();
                            while (results.hasNext()) {
                                Document resultUser = results.next();
                                System.out.println("UserISSS: " + resultUser.getObjectId("_id"));
                                users.add(new ClassicUser(resultUser.getObjectId("_id"), resultUser.getString("name"),
                                        resultUser.getString("email"),
                                        resultUser.getString("password"), resultUser.getString("role"),
                                        Math.toIntExact(resultUser.getLong("level")),
                                        Math.toIntExact(resultUser.getLong("xp")),
                                        trophiesAll,
                                        recycles
                                ));

                            }
                            for (ClassicUser r : users) {
                                Log.v("QUICKSTART", "User: " + r.get_id());
                            }
                        } else {
                            Log.e("QUICKSTART", "failed to find users with: ", task.getError());
                        }
                        recycleRequestItems.getAsync(task1 -> {
                            if (task1.isSuccess()) {
                                MongoCursor<Document> results = task1.get();
                                while (results.hasNext()) {
                                    Document resultRecycleRequest = results.next();
                                    for (Item items : items) {
                                        if (items.get_id().equals(resultRecycleRequest.getObjectId("item"))) {
                                            recycleRequests.add(new RecycleRequest(resultRecycleRequest.getObjectId("_id"),
                                                    resultRecycleRequest.getString("date"),
                                                    items));
                                        }
                                    }
                                }
                                for (RecycleRequest r : recycleRequests) {
                                    Log.v("QUICKSTART", "RecycleRequest: " + r.getDate());
                                }
                            } else {
                                Log.e("QUICKSTART", "failed to find recycleRequests with: ", task1.getError());
                            }
                            requestItems.getAsync(task2 -> {
                                if (task2.isSuccess()) {
                                    MongoCursor<Document> results = task2.get();
                                    while (results.hasNext()) {
                                        Document resultRequest = results.next();
                                        for (RecycleRequest recycleRequest : recycleRequests) {
                                            if (recycleRequest.get_id().equals(resultRequest.getObjectId("recycle"))) {
                                                requests.add(new Request(resultRequest.getObjectId("_id"),
                                                        resultRequest.getObjectId("user"),
                                                        resultRequest.getBoolean("status"),
                                                        recycleRequest));
                                            }
                                        }
                                    }
                                    for (Request r : requests) {
                                        Log.v("QUICKSTART", "Request: " + r.getRecycle().get_id());
                                    }
                                } else {
                                    Log.e("QUICKSTART", "failed to find requests with: ", task2.getError());
                                }
                            });

                            callback.OnGetData();
                        });
                    });
                });
            });
        });
    }

    public interface OnGetDataCallback {
        void OnGetData();
    }
}
