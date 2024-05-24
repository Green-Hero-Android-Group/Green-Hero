package com.example.green_hero.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.green_hero.DB;
import com.example.green_hero.databinding.FragmentProfileBinding;
import com.example.green_hero.R;
import com.example.green_hero.databinding.TrophyLinearBinding;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Trophy;

import io.realm.RealmList;
//import com.example.green_hero.ui.home.HomeViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private TextView name;
    private TextView level;
    private ProgressBar progressBar;
    private TextView nextReward;
    private LinearLayout trophiesLayout;
    private LinearLayout v1;
    private LinearLayout v2;
    private TrophyLinearBinding trophyBinding;
    private int trophyCount;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        View root = binding.getRoot();

        //Padding to make the bottom navigation bar not overlap with the content
        bottomNavigationView.post(() -> {
            int height = bottomNavigationView.getHeight();
            root.setPadding(root.getPaddingLeft(), root.getPaddingTop(), root.getPaddingRight(), height);
        });

        //DB
        ProfileViewModel viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        ClassicUser user = DB.getClassicUser();

        //UI
        name = binding.profileName;
        name.setText(user.getName());

        level = binding.profileLevel;
        level.setText("Level " + user.getLevel().getLevel());

        progressBar = binding.profileLevelBar;
        progressBar.setProgress(user.getXp());

        nextReward = binding.profileNextReward;
        nextReward.setText("Paper Badge");

        // Create trophy layout
        RealmList<Trophy> trophies = user.getTrophies();
        System.out.println(trophies.size());
        for(Trophy trophy : trophies){
            Log.v("QUICKSTART", "Trophy: " + trophy.getName());
        }
        trophiesLayout = binding.profileTrophiesLayout;
        for (Trophy trophy : trophies) {
            LinearLayout v1 = (LinearLayout) inflater.inflate(R.layout.trophy_linear, container, false);
            v1.setId(View.generateViewId());
            trophiesLayout.addView(v1);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
