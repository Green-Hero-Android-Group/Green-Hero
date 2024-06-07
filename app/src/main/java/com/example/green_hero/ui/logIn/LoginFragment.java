package com.example.green_hero.ui.logIn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.green_hero.R;
import com.example.green_hero.databinding.LogInBinding;
//import com.example.green_hero.ui.home.HomeViewModel;

public class LoginFragment extends Fragment {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private LogInBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = LogInBinding.inflate(inflater, container, false);
        View bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        View root = binding.getRoot();

        //Padding to make the bottom navigation bar not overlap with the content
        bottomNavigationView.post(() -> {
            int height = bottomNavigationView.getHeight();
            root.setPadding(root.getPaddingLeft(), root.getPaddingTop(), root.getPaddingRight(), height);
        });

        usernameEditText = root.findViewById(R.id.type_name_input);
        passwordEditText = root.findViewById(R.id.type_text_password);
        loginButton = root.findViewById(R.id.signUpButton);

        //DB
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        return root;

    }

    private void login() {

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.equals("example_user") && password.equals("example_password")) {
            // Εάν η ταυτοποίηση είναι επιτυχής
            Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
            // Εδώ μπορείς να προχωρήσεις σε μια άλλη δραστηριότητα ή να εκτελέσεις κάποιες άλλες ενέργειες
        } else {

            Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
