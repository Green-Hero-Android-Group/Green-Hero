package com.example.green_hero.ui.recycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.green_hero.databinding.FragmentProfileBinding;
import com.example.green_hero.databinding.FragmentRecycleBinding;

public class RecycleFragment extends Fragment {
    private FragmentRecycleBinding binding;
    private RadioGroup rg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecycleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
