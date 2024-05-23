package com.example.green_hero.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.green_hero.DB;
import com.example.green_hero.R;
import com.example.green_hero.databinding.FragmentHomeBinding;
import com.example.green_hero.model.User.ClassicUser;

import org.bson.types.ObjectId;

import io.realm.RealmResults;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    TextView helloMessage;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        //DB
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        ClassicUser user = DB.getClassicUser();


        //Updating the UI
        //Update user's name in Hello message
        helloMessage = binding.helloMessage;
        helloMessage.setText("Hello, " + user.getName() + "!");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}