package com.example.green_hero.ui.manage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.green_hero.DB;
import com.example.green_hero.R;
import com.example.green_hero.databinding.FragmentManageBinding;
import com.example.green_hero.databinding.FragmentProfileBinding;
import com.example.green_hero.model.Admin.RecycleRequest;
import com.example.green_hero.model.Admin.Request;
import com.example.green_hero.model.Recycle.Item;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.ui.recycle.RecycleViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ManageFragment extends Fragment {

    private FragmentManageBinding binding;
    private TextView requestNumber;
    private LinearLayout requestList;
    public static HashMap<ClassicUser, ArrayList<Request>> userRequests;
    public static HashMap<ClassicUser, ArrayList<Item>> userItems;
    public static HashMap<String, String> userCheckBoxes;
    public static HashMap<CardView, ArrayList<CheckBox>> cardViewCheckBoxes;
    public static int requestCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentManageBinding.inflate(inflater, container, false);
        View bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        userRequests = new HashMap<>();
        userItems = new HashMap<>();
        userCheckBoxes = new HashMap<>();
        cardViewCheckBoxes = new HashMap<>();

        View root = binding.getRoot();

        //Padding to make the bottom navigation bar not overlap with the content
        bottomNavigationView.post(() -> {
            int height = bottomNavigationView.getHeight();
            root.setPadding(root.getPaddingLeft(), root.getPaddingTop(), root.getPaddingRight(), height);
        });

        //DB
        RealmResults<Request> requests = DB.realm.where(Request.class).findAll();
        RealmResults<ClassicUser> users = DB.realm.where(ClassicUser.class).findAll();
        RealmResults<RecycleRequest> recycleRequests = DB.realm.where(RecycleRequest.class).findAll();
        RealmResults<Item> items = DB.realm.where(Item.class).findAll();

        System.out.println("Requests: " + requests);
        System.out.println("Users: " + users);

        for (Request request : requests) {
            System.out.println("Manage: " + request);
        }

        //Updating the UI
        //Update the request number
        requestNumber = binding.requestNumber;
        requestCount = 0;
        for (Request request : requests) {
            if (!request.isStatus()) {
                requestCount++;
            }
        }
        requestNumber.setText(requestCount+ " requests");
        System.out.println("Request Count: " + requestCount);

        requestList = binding.requestsLinearLayout;

        //Create a card for each user that has one or multiple requests
        if(!requests.isEmpty()){
            for (ClassicUser user : users) {
                System.out.println("User: " + user.getName() + " ID: " + user.get_id());
                ClassicUser requestUser = null;
                for (Request request : requests) {
                    if (!request.isStatus()) {
                        System.out.println("Request: " + request.get_id() + " Status: " + request.isStatus());
                    }
                    if (user.get_id().equals(request.getUser().get_id())) {
                        requestUser = user;
                        break;
                    }
                }
                if (requestUser != null) {
                    if (userHasRequests(requestUser, requests)) {
                        System.out.println("Request User: " + requestUser.getName());
                        CardView cardView = (CardView) inflater.inflate(R.layout.request_card, container, false);
                        TextView requestText = cardView.findViewById(R.id.requestTitle);
                        LinearLayout checkBoxes = cardView.findViewById(R.id.checkBoxContainer);
                        requestText.setText(requestUser.getName() + " has recycled");
                        ArrayList<CheckBox> currentCheckBoxes = new ArrayList<>();
                        ArrayList<Item> currentItems = new ArrayList<>();
                        ArrayList<Request> currentRequests = new ArrayList<>();
                        for (Request request : requests) {
                            if (!request.isStatus()){
                                if (request.getUser().get_id().equals(requestUser.get_id())) {
                                    currentRequests.add(request);
                                    cardView.setTag(requestUser.get_id());
                                    for(RecycleRequest recycleRequest : recycleRequests){
                                        if(recycleRequest.get_id().equals(request.getRecycle().get_id())){
                                            for(Item item : items){
                                                if(item.get_id().equals(recycleRequest.getItem().get_id())){
                                                    currentItems.add(item);
                                                    CheckBox checkBox = (CheckBox) inflater.inflate(R.layout.request_checkbox, cardView, false);
                                                    checkBox.setTag(item.get_id().toString());
                                                    System.out.println("Tag: " + checkBox.getTag());
                                                    checkBox.setId(View.generateViewId());
                                                    userCheckBoxes.put(checkBox.getTag().toString(), item.get_id().toString());
                                                    System.out.println("ID: " + checkBox.getId());
                                                    checkBox.setText(item.getName());
                                                    checkBoxes.addView(checkBox);
                                                    currentCheckBoxes.add(checkBox);
                                                    System.out.println("CheckBoxes: " + checkBox);
                                                }
                                            }
                                        }
                                    }
                                    cardViewCheckBoxes.put(cardView, currentCheckBoxes);
                                    userItems.put(requestUser, currentItems);
                                    userRequests.put(requestUser, currentRequests);
                                }
                            }
                        }
                        requestList.addView(cardView);
                    }
                }
            }
        }


        return root;
    }

    //Check if the user has any requests(Used to prevent creating a card for a user that has no requests
    public boolean userHasRequests(ClassicUser user, RealmResults<Request> requests) {
        for (Request request : requests) {
            if (!request.isStatus()) {
                if (request.getUser().get_id().equals(user.get_id())) {
                    return true;
                }
            }
        }
        return false;
    }
}
