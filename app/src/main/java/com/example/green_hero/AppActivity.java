package com.example.green_hero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.green_hero.databinding.ActivityAppBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.realm.mongodb.App;
import io.realm.mongodb.User;

public class AppActivity extends AppCompatActivity {
    private ActivityAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_recycle, R.id.navigation_collection, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_app);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void onLogOut(View view) {
        ImageButton logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(v -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(AppActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 0);
        });
    }

    public void navigateToCollection(View view) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_app);
        navController.popBackStack();
        navController.navigate(R.id.navigation_collection);
    }
}
