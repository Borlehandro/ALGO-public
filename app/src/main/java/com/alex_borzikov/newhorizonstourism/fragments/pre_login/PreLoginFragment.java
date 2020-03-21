package com.alex_borzikov.newhorizonstourism.fragments.pre_login;

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

import com.alex_borzikov.newhorizonstourism.R;

public class PreLoginFragment extends Fragment implements View.OnClickListener {

    private NavController controller;
    private Button registerButton, loginButton;
    private String language;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language = getArguments().getString("language");
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

        Bundle bundle = new Bundle();
        bundle.putString("language", language);

        switch (view.getId()) {

            case R.id.loginChoice :
                controller.navigate(R.id.toLogin, bundle);
                break;

            case R.id.registerChoice:
                controller.navigate(R.id.toRegistration, bundle);
                break;
        }
    }
}
