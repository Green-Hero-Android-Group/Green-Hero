package com.example.green_hero;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.green_hero.databinding.ActivityAdminBinding;
import com.example.green_hero.databinding.ActivityAppBinding;
import com.example.green_hero.model.Admin.RecycleRequest;
import com.example.green_hero.model.Admin.Request;
import com.example.green_hero.model.Recycle.Item;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.ui.manage.ManageFragment;
import com.example.green_hero.ui.manage.ManageViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    public void onApproveRequest(View view) {
        CardView cardView;
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
        for (CardView cardView1 : cardViewCheckBoxes.keySet()) {
            ArrayList<CheckBox> checkedCheckBoxes = new ArrayList<>();
            checkBoxes = cardViewCheckBoxes.get(cardView1);
            for (ClassicUser user : DB.users) {
                if (user.get_id().toString().equals(cardView1.getTag().toString())) {
                    System.out.println("User: " + user.getName());
                    currentItems = items.get(user);
                    break;
                }
            }

            System.out.println("CheckBoxes Size: " + checkBoxes.size());

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
                for (Request request : requests.get(approvedUser)) {
                    if (!request.isStatus()) {
                        for (RecycleRequest recycleRequest : DB.recycleRequests) {
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
                LinearLayout requestList = findViewById(R.id.requestsLinearLayout);
                System.out.println("CheckBox Counter: " + checkBoxCounter);
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
                        int newRequestCount = checkBoxes.size();
                        TextView requestNumber = findViewById(R.id.requestNumber);
                        requestNumber.setText(newRequestCount + " requests");
                    }
                }
                if (checkBoxes.size() == 0) {
                    cardViewCheckBoxes.remove(cardView1);
                    requestList.removeView(cardView1);
                    cardView1.setVisibility(View.GONE);
                }
            }
        }
    }
}
