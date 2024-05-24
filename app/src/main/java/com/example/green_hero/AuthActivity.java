package com.example.green_hero;

import static com.example.green_hero.DB.initializeRealm;
//import static com.example.green_hero.DB.initializeRealmSignIn;
import static com.example.green_hero.DB.loginSync;
import static com.example.green_hero.DB.signUpSync;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
        EditText password2 = findViewById(R.id.type_text_password2);
        logInButton.setOnClickListener(new View.OnClickListener() {
            Class routeClass;

            @Override
            public void onClick(View v) {
                boolean found = false;
                if (username.getText().toString().equals("admin")) {
                    routeClass = AdminActivity.class;
                } else {
                    loginSync(Credentials.emailPassword(username.getText().toString(),
                            password.getText().toString()), new DB.OnUserLoginCallback() {
                        @Override
                        public void onUserLoggedIn(User user) {
                            if (user != null) {
                                System.out.println("Successfully logged in as: " + user.isLoggedIn());
                                initializeRealm(user);
                                routeClass = AppActivity.class;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(AuthActivity.this, routeClass);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 0);
                            } else {
                                Log.e("QUICKSTART", "Failed to log in.");
                                Toast.makeText(AuthActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    routeClass = AppActivity.class;
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
                TextView password = findViewById(R.id.type_text_passwords);
                TextView email = findViewById(R.id.type_text_email);
                TextView password2 = findViewById(R.id.type_text_password2);

                String emailText = email.getText().toString();
                String pass1 = password.getText().toString();
                String pass2 = password2.getText().toString();

                int passwordLength = pass1.length();

                if (!isValidEmail(emailText)) {
                    showRedToast("Invalid email address");
                    email.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));
                } else if (passwordLength < 6) {
                    Log.e("QUICKSTART", "Password smaller than 6 characters");
                    showRedToast("Password must be more than 6 characters");
                    password.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));
                } else {
                    User user = signUpSync(username.getText().toString(),email.getText().toString(), password.getText().toString(), AuthActivity.this,new DB.OnUserLoginCallback() {
                        @Override
                        public void onUserLoggedIn(User user) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(AuthActivity.this, AppActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 0);
                        }
                    });

                }
            }
        });
    }


    private boolean isValidEmail(String email) {

        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    private void showRedToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        toast.show();
    }
}
