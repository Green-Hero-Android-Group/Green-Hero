package com.example.green_hero;

import static com.example.green_hero.DB.app;
import static com.example.green_hero.DB.googleSignInSync;
import static com.example.green_hero.DB.initializeRealm;
import static com.example.green_hero.DB.loginSync;
import static com.example.green_hero.DB.realm;
import static com.example.green_hero.DB.setupRealmForUser;
import static com.example.green_hero.DB.signUpSync;
import static com.example.green_hero.DB.subscribeOnStart;

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

import com.example.green_hero.model.User.ClassicUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import io.realm.Realm;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.GoogleAuthType;
import io.realm.mongodb.sync.SyncConfiguration;

// This class is responsible for handling the authentication process.

public class AuthActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.log_in);
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("294412243304-svsmddbef1cdkctqr1thjueql4e6svks.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.d("AUTH", "GoogleSignInClient configured successfully: " + googleSignInClient.toString());


        // Register ActivityResultLauncher in onCreate
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                }
        );

    }

    // This method is called when the user clicks the "Log in" button.
    public void onLogInClicked(View v) {
        EditText email = findViewById(R.id.type_name_input);
        EditText password = findViewById(R.id.type_text_password);

        //Created tv1 and tv2 for the checkFieldValidation method
        TextView tv1 = new TextView(this);
        TextView tv2 = new TextView(this);
        tv1.setText("login");
        tv2.setText("login");
        if (checkFieldValidation(tv1, email, password, tv2)) {
            if (email.getText().toString().equals("admin@gh.com")) {
                onLogInAdmin(email.getText().toString(), password.getText().toString());
            } else {
                onLogInClassicUser(email.getText().toString(), password.getText().toString());
            }
        }

    }

    // This method is called when a ClassicUser tries to log in.
    public void onLogInClassicUser(String username, String password) {
        loginSync(Credentials.emailPassword(username, password),
                new DB.OnUserLoginCallback() {
                    @Override
                    public void onUserLoggedIn(User user) {
                        if (user != null) {
                            System.out.println("Successfully logged in as: " + user.isLoggedIn());
                            if (realm == null) {
                                initializeRealm(user);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(AuthActivity.this, AppActivity.class);
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
    }

    // This method is called when an Admin tries to log in.
    public void onLogInAdmin(String username, String password) {
        loginSync(Credentials.emailPassword(username, password),
                new DB.OnUserLoginCallback() {
                    @Override
                    public void onUserLoggedIn(User user) {
                        if (user != null) {
                            System.out.println("Successfully logged in as: " + user.isLoggedIn());
                            if (realm == null) {
                                initializeRealm(user);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(AuthActivity.this, AdminActivity.class);
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
    }

    // This method is called when the user is in the sign up screen and clicks the "Log In" button.
    public void navigateToLogIn(View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.log_in);
            }
        }, 300);
    }

    // This method is called when the user is in the log in screen and clicks the "Register Here" button.
    public void navigateToSignUp(View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.sign_up);
            }
        }, 300);
    }

    // This method is called when the user is in the sign up screen and clicks the "Sign Up" button.
    public void signUp(View v) {
        TextView username = findViewById(R.id.type_name_input);
        TextView password = findViewById(R.id.type_text_passwords);
        TextView email = findViewById(R.id.type_text_email);
        TextView password2 = findViewById(R.id.type_text_password2);

        if (checkFieldValidation(username, email, password, password2)) {
            User user = signUpSync(username.getText().toString(), email.getText().toString(), password.getText().toString(), AuthActivity.this, new DB.OnUserLoginCallback() {
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

    // This method is called when the user clicks the "Sign Up with Google" button.
    public void onSignUpWithGoogle(View v) {
        signInWithGoogle();
    }

    // Handling the sign-in process with Google
    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        resultLauncher.launch(signInIntent);
    }

    // Handling the result of the sign-in process with Google
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d("AUTH", "Handling Google sign-in result");
        Log.d("AUTH", "Result"+ completedTask.isSuccessful());

        try {
            if (completedTask.isSuccessful()) {
                Log.d("AUTH", "Google sign-in successful");
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                String token = account.getIdToken();
                String email=account.getEmail();
                String name= account.getDisplayName();

                Log.v("AUTH","Name "+name);
                Log.v("AUTH","Email "+email);

                Credentials googleCredentials =
                        Credentials.google(token, GoogleAuthType.ID_TOKEN);
                googleSignInSync(googleCredentials,name, email, "123456", this, new DB.OnUserLoginCallback() {


                    @Override
                    public void onUserLoggedIn(User user) {
                        if (user != null) {
                            Log.v("AUTH", "Successfully logged in to MongoDB Realm using Google OAuth.");
//                            Intent intent = new Intent(AuthActivity.this, AppActivity.class);
//                            startActivity(intent);
                            AuthActivity.this.startActivity(new Intent(AuthActivity.this, AppActivity.class));

                        } else {
                            signUpSync(name, email, "123456", AuthActivity.this, new DB.OnUserLoginCallback() {
                                @Override
                                public void onUserLoggedIn(User newUser) {
                                    if (newUser != null) {
                                        SyncConfiguration.InitialFlexibleSyncSubscriptions
                                                handler = subscribeOnStart();
                                        // Εάν ο νέος χρήστης δημιουργήθηκε επιτυχώς, δημιουργήστε την SyncConfiguration
                                        SyncConfiguration flexibleSyncConfig = new SyncConfiguration.Builder(newUser)
                                                .initialSubscriptions(handler)
                                                .allowQueriesOnUiThread(true)
                                                .allowWritesOnUiThread(true)
                                                .build();
                                        realm = Realm.getInstance(flexibleSyncConfig);
                                        // Εκτελέστε τις απαιτούμενες εργασίες
                                    } else {
                                        Log.e("AUTH", "Failed to create new user.");
                                        // Χειριστείτε την αποτυχία δημιουργίας νέου χρήστη
                                    }
                                }
                            });
                        }
                    }

                });
            } else {
                Log.e("AUTH", "Google sign-in failed: " + completedTask.getException().toString());
            }
        } catch (ApiException e) {
            Log.w("AUTH", "Failed to handle Google sign-in result: " + e.getMessage());
        }
    }




    // This method validates the email address with a regex.
    private boolean isValidEmail(String email) {

        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    // This method turns invalid fields red.
    private void showRedToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        toast.show();
    }

    // This method checks the validation of the fields in the sign up and log in screens.
    public boolean checkFieldValidation(TextView name, TextView email,
                                        TextView password, TextView password2) {
        //If the user is logging in
        //Else the user is signing up
        if (name.getText().toString().equals("login") && password2.getText().toString().equals("login")) {
            // Check validation for login fields
            if (!isValidEmail(email.getText().toString())) {
                showRedToast("Invalid email address");
                email.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));
                return false;
            } else if (password.length() < 6) {
                Log.e("QUICKSTART", "Password smaller than 6 characters");
                showRedToast("Password must be more than 6 characters");
                password.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));
                return false;
            }
        } else {
            // Check validation for sign up fields
            if (name.getText().toString().isEmpty()) {
                Log.e("QUICKSTART", "Name field is empty");
                showRedToast("Name field is empty");
                name.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));
                return false;
            } else if (!isValidEmail(email.getText().toString())) {
                showRedToast("Invalid email address");
                email.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));
                return false;
            } else if (password.length() < 6) {
                Log.e("QUICKSTART", "Password smaller than 6 characters");
                showRedToast("Password must be more than 6 characters");
                password.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));
                return false;
            } else if (!password.getText().toString().equals(password2.getText().toString())) {
                Log.e("QUICKSTART", "Different passwords");
                showRedToast("Please type the same password");
                password2.setBackground(ContextCompat.getDrawable(AuthActivity.this, R.drawable.red_border));
                return false;
            }
        }
        return true;
    }
}
