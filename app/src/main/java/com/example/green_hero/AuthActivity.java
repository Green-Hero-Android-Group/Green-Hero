package com.example.green_hero;

import static com.example.green_hero.DB.initializeRealm;
import static com.example.green_hero.DB.loginSync;
import static com.example.green_hero.DB.signUpSync;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Level;

import io.realm.Realm;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class AuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.log_in);
    }

    public void onLogInClicked(View v) {
        Button logInButton = findViewById(R.id.signUpButton);
        EditText username = findViewById(R.id.type_name_input);
        EditText password = findViewById(R.id.type_text_password);
        logInButton.setOnClickListener(new View.OnClickListener() {
            Class routeClass;

            @Override
            public void onClick(View v) {
                boolean found = false;
                if (username.getText().toString().equals("admin")) {
                    routeClass = AdminActivity.class;
                } else {
                    User user = loginSync(Credentials.emailPassword(username.getText().toString(),
                            password.getText().toString()));
                    if (user != null) {
                        System.out.println("Successfully logged in as: " + user.isLoggedIn());
                        found = true;
                        initializeRealm(user);
                    } else {
                        Log.e("QUICKSTART", "Failed to log in.");
                    }
                    if (!found) {
                        Toast.makeText(AuthActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                    routeClass = AppActivity.class;
                }
                if (found) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(AuthActivity.this, routeClass);
                            startActivity(intent);
                            finish();
                        }
                    }, 1000);
                }
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

    public void onSignUpClick(View v) {
        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean found = false;
                TextView username = findViewById(R.id.type_name_input);
                TextView password = findViewById(R.id.type_text_password);
                TextView email = findViewById(R.id.type_text_email);

                String emailText = email.toString();

                int passwordl=password.length();

                if(testPassword(passwordl) && isValidEmail(emailText)){
                    User user = signUpSync(email.getText().toString(), password.getText().toString());
                    initializeRealm(user);
                    DB.realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            ClassicUser user = new ClassicUser("Maria", email.getText().toString(),
                                    password.getText().toString(), "user", new Level(0, 0), 0);
                            realm.insert(user);
                            Log.v("QUICKSTART", "Successfully inserted user.");
                        }
                    });
                    System.out.println("Successfully signed up as: " + user.isLoggedIn());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(AuthActivity.this, AppActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1000);
                } else if (!isValidEmail(emailText)) {
                        showRedToast("Invalid email address");
                        email.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));

                } else {
                    Log.e("QUICKSTART", "Password smaller than 6 characters");
                    showRedToast("Password must be more than 6 characters");
                    password.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));

                }


            }
        });
    }

    private boolean testPassword(int password) {
        return password >= 6;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
    private void showRedToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        toast.show();
    }
}
