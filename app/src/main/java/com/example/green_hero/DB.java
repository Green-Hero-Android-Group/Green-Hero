
package com.example.green_hero;

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

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

// This class is used to manage the database connection, implement some functionality and store static data

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
        createStaticData();
    }

    // This method is used to create some static data for the trophies and rewards
    public void createStaticData() {
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

    // This method is used to login a user
    public static User loginSync(Credentials credentials, OnUserLoginCallback callback) {
        app.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated.");
                callback.onUserLoggedIn(app.currentUser());
            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: " + it.getError().getErrorMessage());
                callback.onUserLoggedIn(null);
            }
        });
        return app.currentUser();
    }

    // Callback for the Async login
    public interface OnUserLoginCallback {
        void onUserLoggedIn(User user);
    }

    public static User googleSignInSync(Credentials cr,String name, String email, String password, Context c, OnUserLoginCallback callback) {
        Credentials credentials1 = Credentials.emailPassword(email, password);

        app.loginAsync(cr, it3 -> {
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
            if(loggedInUser!=null) {
                SyncConfiguration flexibleSyncConfig = new SyncConfiguration.Builder(loggedInUser)
                        .initialSubscriptions(handler)
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();

                realm = Realm.getInstance(flexibleSyncConfig);}
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


    // This method is used to sign up a user
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
                        SyncConfiguration.InitialFlexibleSyncSubscriptions
                                handler = subscribeOnStart();


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

    // This method is used to create initial subscriptions for the database schema
    // (If you don't have any subscriptions, you can't do transactions with the model objects)
    public static SyncConfiguration.InitialFlexibleSyncSubscriptions subscribeOnStart() {
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
        return handler;
    }

    // This method is used to get the current classic user
    public static ClassicUser getClassicUser() {
        RealmResults<ClassicUser> users = realm.where(ClassicUser.class).findAll();
        for (ClassicUser user : users) {
            if (user.getEmail().equals(DB.app.currentUser().getProfile().getEmail())) {
                return user;
            }
        }
        return null;
    }

    // This method is used to initialize the realm database
    static void initializeRealm(User user) {
        SyncConfiguration.InitialFlexibleSyncSubscriptions handler = subscribeOnStart();

        SyncConfiguration flexibleSyncConfig = new SyncConfiguration.Builder(user)
                .initialSubscriptions(handler)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();

        realm = Realm.getInstance(flexibleSyncConfig);
    }

    public static User googleSignInSync(String name, String email, Context c, OnUserLoginCallback callback) {
        String tempPassword = "123456"; // Προσωρινός κωδικός πρόσβασης

        // Δοκιμάζουμε να συνδεθούμε με email και προσωρινό κωδικό πρόσβασης
        Credentials credentials = Credentials.emailPassword(email, tempPassword);
        app.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated with email/password.");
                User loggedInUser = app.currentUser();
                setupRealmForUser(loggedInUser, name, email, tempPassword);
                callback.onUserLoggedIn(loggedInUser);
            } else {
                Log.e("QUICKSTART", "Failed to log in with email/password. Error: " + it.getError().getErrorMessage());

                // Αν η σύνδεση αποτύχει, προσπαθούμε να καταχωρίσουμε τον χρήστη
                app.getEmailPassword().registerUserAsync(email, tempPassword, result -> {
                    if (result.isSuccess()) {
                        Log.v("QUICKSTART", "Successfully registered user.");

                        // Προσπαθούμε να συνδεθούμε ξανά μετά την εγγραφή
                        app.loginAsync(credentials, it2 -> {
                            if (it2.isSuccess()) {
                                Log.v("QUICKSTART", "Successfully authenticated after registration.");
                                User loggedInUser = app.currentUser();
                                setupRealmForUser(loggedInUser, name, email, tempPassword);
                                callback.onUserLoggedIn(loggedInUser);
                            } else {
                                Log.e("QUICKSTART", "Failed to log in after registration. Error: " + it2.getError().getErrorMessage());
                            }
                        });
                    } else {
                        Log.e("QUICKSTART", "Failed to register user. Error: " + result.getError().getErrorMessage());
                    }
                });
            }
        });
        return app.currentUser();
    }

    static void setupRealmForUser(User loggedInUser, String name, String email, String password) {
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
                ClassicUser user = new ClassicUser(name, email, password, "user", 1, 0);
                realm.insert(user);
                Log.v("QUICKSTART", "Successfully inserted user.");
            }
        });
    }


//    public static User googleSignInSync(String name, String email, String password, Context c, OnUserLoginCallback callback) {
//        Credentials credentials1 = Credentials.emailPassword(email, password);
//
//
//        app.loginAsync(credentials1, it3 -> {
//            if (it3.isSuccess()) {
//                Log.v("QUICKSTART", "Successfully authenticated Google.");
//            } else {
//                Log.e("QUICKSTART", "Failed to log in. Error: " + it3.getError().getErrorMessage());
//            }
//            User loggedInUser = app.currentUser();
//            SyncConfiguration.InitialFlexibleSyncSubscriptions handler = new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
//                @Override
//                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
//                    subscriptions.addOrUpdate(
//                            Subscription.create(
//                                    realm.where(ClassicUser.class)
//                            )
//                    );
//                    subscriptions.addOrUpdate(
//                            Subscription.create(
//                                    realm.where(Trophy.class)
//                            )
//                    );
//                }
//            };
//            SyncConfiguration flexibleSyncConfig = new SyncConfiguration.Builder(loggedInUser)
//                    .initialSubscriptions(handler)
//                    .allowQueriesOnUiThread(true)
//                    .allowWritesOnUiThread(true)
//                    .build();
//
//            realm = Realm.getInstance(flexibleSyncConfig);
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    ClassicUser user = new ClassicUser(name, email,
//                            password, "user", 1, 0);
//                    realm.insert(user);
//                    Log.v("QUICKSTART", "Successfully inserted user.");
//                }
//            });
//            System.out.println("Successfully signed up as: " + loggedInUser.isLoggedIn());
//            callback.onUserLoggedIn(loggedInUser);
//        });
//        return app.currentUser();
//    }
}
