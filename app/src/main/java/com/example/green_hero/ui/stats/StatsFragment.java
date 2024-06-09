package com.example.green_hero.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.green_hero.DB;
import com.example.green_hero.R;
import com.example.green_hero.databinding.FragmentHomeBinding;
import com.example.green_hero.databinding.FragmentStatsBinding;
import com.example.green_hero.model.Recycle.Recycle;
import com.example.green_hero.model.User.ClassicUser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.User;

public class StatsFragment extends Fragment {
    private FragmentStatsBinding binding;
    private TableLayout tableLayout;
    private TextView recycledItems;
    private TextView topMaterial;
    private TextView topMaterialRecycles;
    private TextView topUser;
    private TextView topUserRecycles;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

//        View bottomNavigationView = getActivity().findViewById(R.id.nav_view);
//        bottomNavigationView.post(() -> {
//            int height = bottomNavigationView.getHeight();
//            root.setPadding(root.getPaddingLeft(), root.getPaddingTop(), root.getPaddingRight(), height);
//        });

        //DB
        RealmResults<ClassicUser> users = DB.realm.where(ClassicUser.class).findAll();
        RealmResults<Recycle> recycles = DB.realm.where(Recycle.class).findAll();
        int paperCounter = 0;
        int plasticCounter = 0;
        int glassCounter = 0;

        //Updating the UI
        topUser = binding.hotwName;
        topUserRecycles = binding.hothRecycles;
        recycledItems = binding.numOfRecycledItems;
        topMaterial = binding.topMaterial;
        topMaterialRecycles = binding.topMaterialRecycles;

        //Calculating the top user of the week
        //Finding the recycles of the current week
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = monday.plusDays(6);

        int maxRecycles = 0;
        ClassicUser hero = null;
        for (ClassicUser user : users) {
            if (!user.getName().equals("Admin")) {
                System.out.println(user.getName());
                RealmList<Recycle> currentWeekRecycles = new RealmList<>();
                System.out.println(user.getName());
                for (Recycle recycle : user.getRecycles()) {
                    LocalDate date = LocalDate.parse(recycle.getDate());
                    if (date.isAfter(monday) && date.isBefore(sunday) || date.isEqual(monday) || date.isEqual(sunday)){
                        currentWeekRecycles.add(recycle);
                    }
                }
                if (currentWeekRecycles.size() > maxRecycles) {
                    maxRecycles = currentWeekRecycles.size();
                    hero = user;
                }
            }
        }
        //If there are no users or recycles, display "No users" and "No recycles".
        //Else display the top user of the week
        if (users.size() == 1 || recycles.isEmpty()) {
            topUser.setText("No users");
            topUserRecycles.setText("No recycles");
            recycledItems.setText("0");
            topMaterial.setText("None");
            topMaterialRecycles.setText("0");
        } else {

            //Display the top user of the week
            topUser.setText(hero.getName().toString());
            topUserRecycles.setText("with " + String.valueOf(maxRecycles) + " recycles");

            //Display the total number of recycles
            recycledItems.setText(String.valueOf(recycles.size()));

            //Finding the top material
            for (Recycle recycle : recycles) {
                if (recycle.getItem().getType().equals("Paper")) {
                    paperCounter++;
                } else if (recycle.getItem().getType().equals("Plastic")) {
                    plasticCounter++;
                } else if (recycle.getItem().getType().equals("Glass")) {
                    glassCounter++;
                }
            }

            String topMaterialString = "";
            int max = 0;
            for (int i = 0; i < 2; i++) {
                if (paperCounter > max) {
                    max = paperCounter;
                    topMaterialString = "Paper";
                }
                if (plasticCounter > max) {
                    max = plasticCounter;
                    topMaterialString = "Plastic";
                }
                if (glassCounter > max) {
                    max = glassCounter;
                    topMaterialString = "Glass";
                }
            }
            topMaterial.setText(topMaterialString);
            topMaterialRecycles.setText(String.valueOf(max));

            //Creating the leaderboard
            tableLayout = binding.leaderboard;
            RealmResults<ClassicUser> orderedUsers = DB.realm.where(ClassicUser.class).notEqualTo("name", "Admin").findAll();
            Comparator<ClassicUser> compareByRecycles = (user1, user2) -> Integer.compare(user2.getRecycles().size(),
                    user1.getRecycles().size());

            //Ordering the users by the number of recycles
            ArrayList<ClassicUser> sortedUsers = new ArrayList<>(orderedUsers);

            Collections.sort(sortedUsers, compareByRecycles);

            //Creating the leaderboard, by inflating the leaderboard_row layout for each user
            for (ClassicUser user : sortedUsers) {
                if (!user.getName().equals("Admin")) {
                    System.out.println(user.getName());
                    TableRow row = (TableRow) inflater.inflate(R.layout.leaderboard_row, container, false);
                    TextView name = row.findViewById(R.id.heroName);
                    TextView score = row.findViewById(R.id.heroScore);
                    TextView rank = row.findViewById(R.id.heroRank);
                    name.setText(user.getName());
                    score.setText(String.valueOf(user.getRecycles().size()));
                    rank.setText(String.valueOf(sortedUsers.indexOf(user) + 1));
                    tableLayout.addView(row);
                }
            }
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
