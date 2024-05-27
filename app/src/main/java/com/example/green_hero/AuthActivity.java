package com.example.green_hero;

import static com.example.green_hero.DB.app;
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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.example.green_hero.model.User.*;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.GoogleAuthType;

public class AuthActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> resultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.log_in);

        checkGooglePlayServicesAvailability();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("294412243304-svsmddbef1cdkctqr1thjueql4e6svks.apps.googleusercontent.com")
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.d("AUTH", "GoogleSignInClient configured: " + googleSignInClient);


        // Register ActivityResultLauncher in onCreate
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    Log.d("AUTH", "task"+ task.toString());
                    handleSignInResult(task);
                }

        );



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
                if (username.getText().toString().equals("admin@gh.com")) {
                    loginSync(Credentials.emailPassword(username.getText().toString(),
                            password.getText().toString()), new DB.OnUserLoginCallback() {
                        @Override
                        public void onUserLoggedIn(User user) {
                            if (user != null) {
                                System.out.println("Successfully logged in as: " + user.isLoggedIn());
                                initializeRealm(user);
                                routeClass = AdminActivity.class;
                                DB.getRequests(new DB.OnGetDataCallback() {
                                    @Override
                                    public void OnGetData() {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(AuthActivity.this, routeClass);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, 0);
                                    }
                                });
                            } else {
                                Log.e("QUICKSTART", "Failed to log in.");
                                Toast.makeText(AuthActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
                } else if (!pass1.equals(pass2)) {
                    Log.e("QUICKSTART", "Different passwords");
                    showRedToast("Please type the same password");
                    password2.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));


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

    public void onSignUpWithGoogle(View v) {
        signInWithGoogle();
    }

//    private void signInWithGoogle() {
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        resultLauncher.launch(signInIntent);
//    }


//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            if (completedTask.isSuccessful()) {
//                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//                String token = account.getIdToken();
//                Credentials googleCredentials =
//                        Credentials.google(token, GoogleAuthType.ID_TOKEN);
//                app.loginAsync(googleCredentials, it -> {
//                    if (it.isSuccess()) {
//                        Intent intent = new Intent(AuthActivity.this, AppActivity.class);
//                        startActivity(intent);
//
//                        Log.v("AUTH",
//                                "Successfully logged in to MongoDB Realm using Google OAuth.");
//                    } else {
//                        Log.e("AUTH",
//                                "Failed to log in to MongoDB Realm: ", it.getError());
//                    }
//                });
//            } else {
//                Log.e("AUTH", "Google Auth failed: "
//                        + completedTask.getException().toString());
//            }
//        } catch (ApiException e) {
//            Log.w("AUTH", "Failed to log in with Google OAuth: " + e.getMessage());
//        }
//    }



    private boolean isValidEmail(String email) {

        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    private void showRedToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        toast.show();
    }




    private void checkGooglePlayServicesAvailability() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode == ConnectionResult.SUCCESS) {
            // Google Play Services is available
            Log.d("GooglePlayServices", "Google Play Services is available.");
        } else {
            // Google Play Services is not available
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                // There's an error that can be resolved by the user
                googleApiAvailability.getErrorDialog(this, resultCode, 9001).show();
            } else {
                // The error cannot be resolved by the user
                Log.e("GooglePlayServices", "Google Play Services is not available. Error code: " + resultCode);
            }
        }
    }

    private static final String TAG = "GoogleSignInDebug";

    private void signInWithGoogle() {
        Log.d(TAG, "Attempting Google sign-in");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        resultLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "Handling Google sign-in result");
        Log.d(TAG, "Result"+ completedTask.isSuccessful());

        try {
            if (completedTask.isSuccessful()) {
                Log.d(TAG, "Google sign-in successful");
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                String token = account.getIdToken();
                String email=account.getEmail();
                String name= account.getDisplayName();

                Log.e(TAG,"Name"+name);
                Log.e(TAG,"Email"+email);

                Credentials googleCredentials =
                        Credentials.google(token, GoogleAuthType.ID_TOKEN);
                app.loginAsync(googleCredentials, it -> {
                    if (it.isSuccess()) {
                        Log.d(TAG, "Successfully logged in to MongoDB Realm using Google OAuth.");
                        Intent intent = new Intent(AuthActivity.this, AppActivity.class);
                        startActivity(intent);
                    } else {
                        Log.e(TAG, "Failed to log in to MongoDB Realm: " + it.getError());
                    }
                });
            } else {
                Log.e(TAG, "Google sign-in failed: " + completedTask.getException().toString());
            }
        } catch (ApiException e) {
            Log.w(TAG, "Failed to handle Google sign-in result: " + e.getMessage());
        }
    }
}