package com.example.green_hero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.log_in);
    }

    public void onLogInPressed(View v) {
        Button logInButton = findViewById(R.id.loginButton);
        EditText username = findViewById(R.id.type_name_input);
        logInButton.setOnClickListener(new View.OnClickListener() {
            Class routeClass;
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("admin")) {
                    routeClass = AdminActivity.class;
                } else {
                    routeClass = AppActivity.class;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AuthActivity.this, routeClass);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }
        });
    }

    public void onSignInClick(View v) {
        TextView signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setContentView(R.layout.log_in);
                    }
                }, 300);
            }
        });
    }

    public void onRegisterClick(View v) {
        Button registerButton = findViewById(R.id.signInButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setContentView(R.layout.sign_up);
                    }
                }, 300);
            }
        });
    }
}
