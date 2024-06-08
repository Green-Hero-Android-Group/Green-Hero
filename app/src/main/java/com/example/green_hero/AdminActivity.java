package com.example.green_hero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.green_hero.databinding.ActivityAdminBinding;
import com.example.green_hero.model.Admin.RecycleRequest;
import com.example.green_hero.model.Admin.Request;
import com.example.green_hero.model.Recycle.Item;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.ui.manage.ManageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmResults;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;
    private AdminViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_stats, R.id.navigation_manage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin);
        NavigationUI.setupWithNavController(binding.navView, navController);

        viewModel = new AdminViewModel();
    }

    public void onRejectRequest(View view) {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        ArrayList<Item> currentItems = new ArrayList<>();
        HashMap<ClassicUser, ArrayList<Request>> requests = ManageFragment.userRequests;
        HashMap<ClassicUser, ArrayList<Item>> items = ManageFragment.userItems;
        HashMap<String, String> checkBoxesMap = ManageFragment.userCheckBoxes;
        HashMap<CardView, ArrayList<CheckBox>> cardViewCheckBoxes = ManageFragment.cardViewCheckBoxes;


        Item approvedItem = null;
        ClassicUser approvedUser = null;
        Request approvedRequest = null;
        int checkBoxCounter = 0;
        for (int k = 0; k < cardViewCheckBoxes.size(); k++) {
            CardView cardView1 = (CardView) cardViewCheckBoxes.keySet().toArray()[k];
            ArrayList<CheckBox> checkedCheckBoxes = new ArrayList<>();
            checkBoxes = cardViewCheckBoxes.get(cardView1);
            RealmResults<ClassicUser> users = DB.realm.where(ClassicUser.class).findAll();
            for (ClassicUser user : users) {
                if (user.get_id().toString().equals(cardView1.getTag().toString())) {
                    currentItems = items.get(user);
                    break;
                }
            }

            LinearLayout requestList = findViewById(R.id.requestsLinearLayout);
            for (int j = 0; j < checkBoxes.size(); j++) {
                CheckBox checkBox1 = checkBoxes.get(j);
                String item = checkBoxesMap.get(checkBox1.getTag().toString());

                // Find the item that the checkbox corresponds to
                for (Item item1 : currentItems) {
                    if (item1.get_id().toString().equals(item)) {
                        approvedItem = item1;
                        System.out.println("Approved Item: " + approvedItem.getName());
                    }
                }
                // Find the user that the item corresponds to
                for (ClassicUser user : items.keySet()) {
                    ArrayList<Item> items1 = items.get(user);
                    for (Item item1 : items1) {
                        if (item1.getName().equals(approvedItem.getName())) {
                            System.out.println("Approved User: " + user.getName());
                            approvedUser = user;
                        }
                    }
                }
                // Find the request that the user corresponds to
                RealmResults<RecycleRequest> recycleRequests = DB.realm.where(RecycleRequest.class).findAll();
                for (Request request : requests.get(approvedUser)) {
                    if (!request.isStatus()) {
                        for (RecycleRequest recycleRequest : recycleRequests) {
                            if (recycleRequest.get_id().equals(request.getRecycle().get_id())) {
                                if (approvedItem.get_id().equals(recycleRequest.getItem().get_id())) {
                                    System.out.println("Approved Request: " + request.get_id().toString());
                                    approvedRequest = request;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (checkBox1.isChecked()) {
                    checkedCheckBoxes.add(checkBox1);
                }

                for(int i = 0; i < checkedCheckBoxes.size(); i++){
                    CheckBox checkBox = checkedCheckBoxes.get(i);
                    if (checkBox.isChecked()) {
                        System.out.println("approvedItem: " + approvedItem.getName());
                        LinearLayout checkBoxContainer = cardView1.findViewById(R.id.checkBoxContainer);
                        checkBoxContainer.removeView(checkBox);
                        viewModel.rejectRequest(approvedItem, approvedUser, approvedRequest);
                        checkBoxCounter++;
                        checkBoxes.remove(checkBox);
                        checkedCheckBoxes.remove(checkBox);
                        TextView requestNumber = findViewById(R.id.requestNumber);
                        String[] request = requestNumber.getText().toString().split(" ");
                        int newRequestCount = Integer.parseInt(request[0]) - 1;
                        requestNumber.setText(newRequestCount + " requests");
                        System.out.println("CheckBox count" + checkBoxes.size());
                    }
                }
            }
            if (checkBoxes.isEmpty()) {
                cardViewCheckBoxes.remove(cardView1);
                requestList.removeView(cardView1);
                cardView1.setVisibility(View.GONE);
            }
        }
    }

    public void onApproveRequest(View view) {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        ArrayList<Item> currentItems = new ArrayList<>();
        HashMap<ClassicUser, ArrayList<Request>> requests = ManageFragment.userRequests;
        HashMap<ClassicUser, ArrayList<Item>> items = ManageFragment.userItems;
        HashMap<String, String> checkBoxesMap = ManageFragment.userCheckBoxes;
        HashMap<CardView, ArrayList<CheckBox>> cardViewCheckBoxes = ManageFragment.cardViewCheckBoxes;


        Item approvedItem = null;
        ClassicUser approvedUser = null;
        Request approvedRequest = null;
        int checkBoxCounter = 0;
        for (int k = 0; k < cardViewCheckBoxes.size(); k++) {
            CardView cardView1 = (CardView) cardViewCheckBoxes.keySet().toArray()[k];
            ArrayList<CheckBox> checkedCheckBoxes = new ArrayList<>();
            checkBoxes = cardViewCheckBoxes.get(cardView1);
            RealmResults<ClassicUser> users = DB.realm.where(ClassicUser.class).findAll();
            for (ClassicUser user : users) {
                if (user.get_id().toString().equals(cardView1.getTag().toString())) {
                    currentItems = items.get(user);
                    break;
                }
            }

            LinearLayout requestList = findViewById(R.id.requestsLinearLayout);
            for (int j = 0; j < checkBoxes.size(); j++) {
                CheckBox checkBox1 = checkBoxes.get(j);
                String item = checkBoxesMap.get(checkBox1.getTag().toString());

                // Find the item that the checkbox corresponds to
                for (Item item1 : currentItems) {
                    if (item1.get_id().toString().equals(item)) {
                        approvedItem = item1;
                        System.out.println("Approved Item: " + approvedItem.getName());
                    }
                }
                // Find the user that the item corresponds to
                for (ClassicUser user : items.keySet()) {
                    ArrayList<Item> items1 = items.get(user);
                    for (Item item1 : items1) {
                        if (item1.getName().equals(approvedItem.getName())) {
                            System.out.println("Approved User: " + user.getName());
                            approvedUser = user;
                        }
                    }
                }
                // Find the request that the user corresponds to
                RealmResults<RecycleRequest> recycleRequests = DB.realm.where(RecycleRequest.class).findAll();
                for (Request request : requests.get(approvedUser)) {
                    if (!request.isStatus()) {
                        for (RecycleRequest recycleRequest : recycleRequests) {
                            if (recycleRequest.get_id().equals(request.getRecycle().get_id())) {
                                if (approvedItem.get_id().equals(recycleRequest.getItem().get_id())) {
                                    System.out.println("Approved Request: " + request.get_id().toString());
                                    approvedRequest = request;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (checkBox1.isChecked()) {
                    checkedCheckBoxes.add(checkBox1);
                }

                for(int i = 0; i < checkedCheckBoxes.size(); i++){
                    CheckBox checkBox = checkedCheckBoxes.get(i);
                    if (checkBox.isChecked()) {
                        System.out.println("approvedItem: " + approvedItem.getName());
                        LinearLayout checkBoxContainer = cardView1.findViewById(R.id.checkBoxContainer);
                        checkBoxContainer.removeView(checkBox);
                        viewModel.approveRequest(approvedItem, approvedUser, approvedRequest);
                        checkBoxCounter++;
                        checkBoxes.remove(checkBox);
                        checkedCheckBoxes.remove(checkBox);
                        TextView requestNumber = findViewById(R.id.requestNumber);
                        String[] request = requestNumber.getText().toString().split(" ");
                        int newRequestCount = Integer.parseInt(request[0]) - 1;
                        requestNumber.setText(newRequestCount + " requests");
                        System.out.println("CheckBox count" + checkBoxes.size());
                    }
                }
            }
            if (checkBoxes.isEmpty()) {
                cardViewCheckBoxes.remove(cardView1);
                requestList.removeView(cardView1);
                cardView1.setVisibility(View.GONE);
            }
        }
    }

    public void onLogOut(View view) {
        ImageButton logOutButton = findViewById(R.id.logOutButtonAdmin);
        logOutButton.setOnClickListener(v -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(AdminActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 0);
        });
    }
}
