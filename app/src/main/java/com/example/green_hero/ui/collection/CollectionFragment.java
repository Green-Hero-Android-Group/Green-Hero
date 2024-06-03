package com.example.green_hero.ui.collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.green_hero.R;
import com.example.green_hero.databinding.FragmentCollectionBinding;
import com.example.green_hero.databinding.FragmentRecycleBinding;

public class CollectionFragment extends Fragment {

    private FragmentCollectionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        View root;
        root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
