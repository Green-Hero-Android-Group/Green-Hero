package com.example.green_hero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.green_hero.databinding.SplashBinding;
import androidx.appcompat.app.AppCompatActivity;

// This class is responsible for the splash screen that appears when the app is launched and the navigation to the Authentication.
public class MainActivity extends AppCompatActivity {

    private SplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}