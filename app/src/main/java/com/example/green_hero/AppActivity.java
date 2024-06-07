package com.example.green_hero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.green_hero.databinding.ActivityAppBinding;
import com.example.green_hero.model.Recycle.Item;
import com.example.green_hero.ui.recycle.RecycleViewModel;
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

        //DB
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
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

    public void onRecycle(View view) {

        onClickRecycle();

    }

    public void onClickRecycle() {
        //DB
        RecycleViewModel viewModel = new ViewModelProvider(this).get(RecycleViewModel.class);
        EditText name = findViewById(R.id.type_name_input);
        String typeName = name.getText().toString().trim();
        EditText qntInput = findViewById(R.id.quantity);
        int qnt = Integer.parseInt(qntInput.getText().toString());

        RadioGroup rg = findViewById(R.id.radioGroup);
        int checkedButton = rg.getCheckedRadioButtonId();

        if(checkedButton == -1 || typeName.isEmpty() || qnt<=0){
            Toast.makeText(AppActivity.this,
                    "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }else{
            String selected = "";
            switch (checkedButton){
                case R.id.paper_button:
                    selected = "Paper";
                    break;
                case R.id.plastic_button:
                    selected = "Plastic";
                    break;
                case R.id.glass_button:
                    selected = "Glass";
                    break;
            }

            Item newItem = new Item(typeName, qnt, selected);

            Toast.makeText(AppActivity.this,
                    "You Recycled: " + newItem.getType() + "\nName: " + newItem.getName() + "\nQuantity: " + newItem.getQuantity(), Toast.LENGTH_SHORT).show();
            viewModel.insertItem(newItem);
        }
        name.getText().clear();
        qntInput.getText().clear();
        rg.clearCheck();
    }
}
