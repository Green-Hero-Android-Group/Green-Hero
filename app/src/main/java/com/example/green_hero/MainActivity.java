package com.example.green_hero;

import android.os.Bundle;
import android.util.Log;

import com.example.green_hero.model.User.ClassicUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.green_hero.databinding.ActivityMainBinding;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private String appID = "application-0-rexuosx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_recycle, R.id.navigation_collection, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);



        //Realm creation
        Realm.init(this);
        App app = new App(new AppConfiguration.Builder(appID)
                .build());
        // authenticate a user
        Credentials credentials = Credentials.emailPassword("dimsparagis@gmail.com", "123456");
        app.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                Log.v("User", "Successfully authenticated");
                User user = it.get();
                // add an initial subscription to the sync configuration
                SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                            @Override
                            public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                subscriptions.addOrUpdate(Subscription.create("subscriptionName",
                                        realm.where(ClassicUser.class)));
                            }
                        })
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();
                // instantiate a realm instance with the flexible sync configuration
                Realm.getInstanceAsync(config, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        Log.v("EXAMPLE", "Successfully opened a realm.");
                    }
                });
                Realm.getInstance(config).close();
            } else {
                Log.e("EXAMPLE", "Failed to log in: " + it.getError().getErrorMessage());
            }
        });
    }
}