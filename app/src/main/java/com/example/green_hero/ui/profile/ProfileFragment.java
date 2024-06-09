package com.example.green_hero.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.green_hero.model.User.Collectible;
import com.example.green_hero.model.User.Trophy;

import io.realm.RealmList;
//import com.example.green_hero.ui.home.HomeViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private TextView name;
    private TextView level;
    private ProgressBar progressBar;
    private TextView nextRewardTitle;
    private LinearLayout trophiesLayout;
    private ImageButton editName;
    private EditText editText;


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

        //Update the UI
        //Updating the profile name
        name = binding.profileName;
        name.setText(user.getName());

        //Updating the profile level
        level = binding.profileLevel;
        System.out.println("Current Level" + user.getLevel());
        level.setText("Level " + user.getLevel());

        //Updating the next reward
        nextRewardTitle = binding.profileNextReward;
        for (Collectible reward : DB.rewards) {
            if (user.getLevel() == reward.getIndex()) {
                nextRewardTitle.setText(reward.getName());
                break;
            }
        }

        //Updating the progress bar
        progressBar = binding.profileLevelBar2;
        progressBar.setProgress(user.getXp());

        // Create trophy layout(There is a LinearLayout in the profile fragment layout for trophies)
        RealmList<Trophy> trophies = user.getTrophies();
        System.out.println(trophies.size());
        for (Trophy trophy : trophies) {
            Log.v("QUICKSTART", "Trophy: " + trophy.getName());
        }
        trophiesLayout = binding.profileTrophiesLayout;
        for (Trophy trophy : trophies) {
            LinearLayout v1 = (LinearLayout) inflater.inflate(R.layout.trophy_linear, container, false);
            TextView trophyTitle = v1.findViewById(R.id.profileTrophyTitle);
            TextView trophyIndex = v1.findViewById(R.id.profileTrophyIndex);
            trophyTitle.setText(trophy.getName());
            trophyIndex.setText(Integer.toString(trophy.getIndex()));
            v1.setId(View.generateViewId());
            trophiesLayout.addView(v1);
        }

        //Add an edit name listener
        editName = binding.profileEditName;
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getVisibility() == View.VISIBLE) {
                    // TextView is visible, replace it with EditText
                    name.setVisibility(View.GONE);
                    ((ViewGroup) name.getParent()).removeView(name); // Remove TextView from parent
                    editText = (EditText) inflater.inflate(R.layout.edit_text_profile, container, false);// Create new EditText
                    ((ViewGroup) editName.getParent()).addView(editText); // Add EditText to parent (assuming same parent)
                    editText.setPadding(0, 0, 0, 0);

                    editText.setText(name.getText().toString()); // Set initial text in EditText
                    editText.requestFocus(); // Request focus for keyboard
                } else {
                    //add an enter click listener
                    // EditText is visible, replace it with TextView
                    name.setVisibility(View.VISIBLE);
                    ((ViewGroup) editText.getParent()).removeView(editText); // Remove EditText from parent
                    name = binding.profileName; // Create new TextView
                    ((ViewGroup) editName.getParent()).addView(name); // Add TextView to parent (assuming same parent)
                    name.setText(editText.getText().toString()); // Set initial text in TextView
                    System.out.println("New name: " + name.getText().toString());
                    viewModel.changeName(name.getText().toString());
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
