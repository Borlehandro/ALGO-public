package com.sibdever.algo_android.fragments.pre_login;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sibdever.algo_android.R;
import com.sibdever.algo_android.activities.PreLoginActivity;

import static android.content.Context.MODE_PRIVATE;

public class PreLoginFragment extends Fragment implements View.OnClickListener {

    private NavController controller;
    private SharedPreferences preferences;

    private Button registerButton, loginButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getActivity().getSharedPreferences("User", MODE_PRIVATE);

        if(preferences.contains("ticket")) {
            // Ok
            ((PreLoginActivity)(getActivity())).checkPermissions();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pre_login, container, false);

        registerButton = v.findViewById(R.id.registerChoice);
        loginButton = v.findViewById(R.id.loginChoice);

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.loginChoice :
                controller.navigate(R.id.toLogin);
                break;

            case R.id.registerChoice:
                controller.navigate(R.id.toRegistration);
                break;
        }
    }
}
