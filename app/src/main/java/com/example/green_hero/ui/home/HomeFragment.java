package com.example.green_hero.ui.home;

import static com.example.green_hero.DB.app;
import static com.example.green_hero.DB.trophies;

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
import com.example.green_hero.model.User.Trophy;
import com.example.green_hero.utils.Actions;
import com.example.green_hero.utils.Transactions;

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
        Trophy firstTrophy = null;
        for (Trophy trophy : trophies) {
            if(trophy.getName().equals("Welcome Hero")){
                firstTrophy = trophy;
            }
        }

//        DB.getTrophies();
//        Trophy finalFirstTrophy = firstTrophy;
//        Transactions.updateUserTrophies(finalFirstTrophy);
        Actions.trophyToast(getContext());

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