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

import io.realm.RealmList;

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

        //DB
        RealmList<ClassicUser> users = DB.users;
        RealmList<Recycle> recycles = DB.recycles;
        int paperCounter = 0;
        int plasticCounter = 0;
        int glassCounter = 0;

        //Updating the UI
        topUser = binding.hotwName;
        topUserRecycles = binding.hothRecycles;
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = monday.plusDays(6);

        int maxRecycles = 0;
        ClassicUser hero = null;
        for (ClassicUser user : users) {
            if (!user.getName().equals("Admin")) {
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

        topUser.setText(hero.getName().toString());
        topUserRecycles.setText("with " + String.valueOf(maxRecycles) + " recycles");


        recycledItems = binding.numOfRecycledItems;
        recycledItems.setText(String.valueOf(recycles.size()));

        topMaterial = binding.topMaterial;
        topMaterialRecycles = binding.topMaterialRecycles;
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

        tableLayout = binding.leaderboard;
        RealmList<ClassicUser> orderedUsers = new RealmList<>();
        for (ClassicUser user : users) {
            if (!user.getName().equals("Admin")) {
                if (orderedUsers.isEmpty()) {
                    orderedUsers.add(user);
                } else {
                    for (int i = 0; i < orderedUsers.size(); i++) {
                        if (user.getRecycles().size() > orderedUsers.get(i).getRecycles().size()) {
                            orderedUsers.add(i, user);
                            break;
                        }
                    }
                }
            }
        }
        for (ClassicUser user : orderedUsers) {
            TableRow row = (TableRow) inflater.inflate(R.layout.leaderboard_row, container, false);
            TextView name = row.findViewById(R.id.heroName);
            TextView score = row.findViewById(R.id.heroScore);
            TextView rank = row.findViewById(R.id.heroRank);
            name.setText(user.getName());
            score.setText(String.valueOf(user.getRecycles().size()));
            rank.setText(String.valueOf(orderedUsers.indexOf(user) + 1));
            tableLayout.addView(row);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
