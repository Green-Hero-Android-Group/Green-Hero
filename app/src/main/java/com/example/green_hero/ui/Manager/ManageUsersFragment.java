package com.example.green_hero.ui.Manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.green_hero.R;
import com.example.green_hero.databinding.FragmentProfileBinding;

import android.view.Window;
import androidx.activity.EdgeToEdge;

public class ManageUsersFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View bottomNavigationView = getActivity().findViewById(R.id.nav_view);

        View view = inflater.inflate(R.layout.fragment_manage_users, container, false);

        //Padding to make the bottom navigation bar not overlap with the content
        bottomNavigationView.post(() -> {
            int height = bottomNavigationView.getHeight();
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), height);
        });



        return view;
    }
}


