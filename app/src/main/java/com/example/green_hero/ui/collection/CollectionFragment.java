package com.example.green_hero.ui.collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.green_hero.DB;
import com.example.green_hero.R;
import com.example.green_hero.databinding.FragmentCollectionBinding;
import com.example.green_hero.databinding.FragmentRecycleBinding;
import com.example.green_hero.model.Recycle.Recycle;
import com.example.green_hero.model.User.ClassicUser;
import com.example.green_hero.model.User.Collectible;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmList;

public class CollectionFragment extends Fragment {

    private FragmentCollectionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        View root;
        root = binding.getRoot();

        //DB
        //Updating the UI
        ClassicUser user = DB.getClassicUser();
        RealmList<Recycle> recycleList = user.getRecycles();
        ArrayList<String> uniqueRecycleList = new ArrayList<>();
        HashMap<String, Integer> uniqueRecycleMap = new HashMap<>();
        //Iterating through the recycle list to get the unique items
        for (Recycle recycle : recycleList) {
            if (!uniqueRecycleList.contains(recycle.getItem().getName())) {
                uniqueRecycleMap.put(recycle.getItem().getName(), recycle.getItem().getQuantity());
                uniqueRecycleList.add(recycle.getItem().getName());
            } else {
                uniqueRecycleMap.put(recycle.getItem().getName(),
                        uniqueRecycleMap.get(recycle.getItem().getName()) + recycle.getItem().getQuantity());
            }
        }
        //Updating the recycle collection
        LinearLayout itemsCollectionLinearLayout = binding.itemsCollectionLinearLayout;
        for(String recycleName : uniqueRecycleMap.keySet()){
            LinearLayout row = (LinearLayout) inflater.inflate(R.layout.item_row_collection, container, false);
            TextView itemName = row.findViewById(R.id.collectionItemName);
            TextView itemQuantity = row.findViewById(R.id.collectionItemQuantity);
            itemName.setText(recycleName);
            itemQuantity.setText("Quantity: " + Integer.toString(uniqueRecycleMap.get(recycleName)));
            itemsCollectionLinearLayout.addView(row);
        }

        //Updating the rewards collection
        LinearLayout rewardsCollectionLinearLayout = binding.rewardsCollectionLayout;
        for (Collectible reward: DB.rewards) {
            if (reward.getIndex() < user.getLevel()) {
                LinearLayout row = (LinearLayout) inflater.inflate(R.layout.item_row_collection, container, false);
                TextView itemName = row.findViewById(R.id.collectionItemName);
                TextView itemQuantity = row.findViewById(R.id.collectionItemQuantity);
                itemName.setText(reward.getName());
                itemQuantity.setText("");
                rewardsCollectionLinearLayout.addView(row);
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
