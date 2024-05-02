package com.example.green_hero.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.green_hero.databinding.FragmentHomeBinding;
import com.example.green_hero.databinding.FragmentProfileBinding;
import com.example.green_hero.R;
//import com.example.green_hero.ui.home.HomeViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;


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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
