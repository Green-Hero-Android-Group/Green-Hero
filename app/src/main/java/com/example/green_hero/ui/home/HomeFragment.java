package com.example.green_hero.ui.home;

import static com.example.green_hero.DB.app;
import static com.example.green_hero.DB.trophies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.green_hero.DB;
import com.example.green_hero.R;
import com.example.green_hero.databinding.FragmentHomeBinding;
import com.example.green_hero.model.Recycle.Recycle;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Collectible;
import com.example.green_hero.model.User.Trophy;
import com.example.green_hero.utils.Actions;
import com.example.green_hero.utils.Transactions;

import org.bson.types.ObjectId;
import org.w3c.dom.Text;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.sync.Progress;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView helloMessage;
    private TextView historyDate1;
    private TextView historyDate2;
    private TextView historyRecycle1;
    private TextView historyRecycle2;
    private TextView level;
    private TextView nextReward;
    private TextView xpUntilNextLevel;
    private ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        //DB
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        ClassicUser user = DB.getClassicUser();

        Trophy firstTrophy = null;
        Trophy secondTrophy = null;
        for (Trophy trophy : trophies) {
            System.out.println(trophy.getName());
            if (trophy.getName().equals("Welcome Hero")) {
                firstTrophy = trophy;
            }
            if (trophy.getName().equals("Reached Level 2")) {
                secondTrophy = trophy;
            }
        }
        for (Trophy trophy : user.getTrophies()) {
            System.out.println(trophy.getName());
            if (trophy.getName().equals("Welcome Hero")) {
                firstTrophy = null;
            }
        }

        if (firstTrophy != null) {
            Transactions.updateUserTrophies(firstTrophy);
            Actions.trophyToast(getContext());
        }

        //Updating the UI
        //Update user's name in Hello message
        helloMessage = binding.helloMessage;
        helloMessage.setText("Hello, " + user.getName() + "!");

        //Updating history
        historyDate1 = binding.historyDate1;
        historyDate2 = binding.historyDate2;
        historyRecycle1 = binding.historyRecycle1;
        historyRecycle2 = binding.historyRecycle2;

        //Getting the 2 most recent recycles
        int recycleSize = user.getRecycles().size();
        if (recycleSize == 1) {
            RealmList<Recycle> oneRecycle = new RealmList<>();
            oneRecycle.add(user.getRecycles().get(0));
        } else if( recycleSize > 1) {
            RealmList<Recycle> twoRecycles = new RealmList<>();
            twoRecycles.add(user.getRecycles().get(recycleSize - 1));
            twoRecycles.add(user.getRecycles().get(recycleSize - 2));

            historyDate1.setText(twoRecycles.get(0).getDate());
            historyDate2.setText(twoRecycles.get(1).getDate());
            historyRecycle1.setText(Integer.toString(twoRecycles.get(1).getItem().getQuantity()) + " " + twoRecycles.get(1).getItem().getName());
            historyRecycle2.setText(Integer.toString(twoRecycles.get(0).getItem().getQuantity()) + " " + twoRecycles.get(0).getItem().getName());
        }

        //Updating level container
        level = binding.lvlHome;
        xpUntilNextLevel = binding.xpTillNext;
        nextReward = binding.nextRewardHome;
        progressBar = binding.homeLevelBar;

        level.setText(Integer.toString(user.getLevel()));
        int xpUntilNextLevelNumber = 100 - user.getXp();
        xpUntilNextLevel.setText(xpUntilNextLevelNumber + " xp until next level");
        for(Collectible collectible : DB.rewards) {
            if (collectible.getIndex() == user.getLevel()) {
                nextReward.setText(collectible.getName());
                break;
            }
        }

        progressBar.setProgress(user.getXp());


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}