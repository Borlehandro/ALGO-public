package com.alex_borzikov.newhorizonstourism.fragments.pre_login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.activities.MainActivity;
import com.alex_borzikov.newhorizonstourism.activities.PreLoginActivity;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.dialogs.PermissionDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginFragment extends Fragment {

    private static final String TAG = "Borlehandro";

    private static int userId;
    private static String userName;
    private static String password;

    private EditText loginUser, loginPassword;

    private Button loginButton;

    private NavController controller;
    private String language;

    static final int REQUEST_ALL_RESULT = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language = getArguments().getString("language");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        loginUser = v.findViewById(R.id.login_user);
        loginPassword = v.findViewById(R.id.login_password);

        loginButton = v.findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            Log.d(TAG, "Login");
            if (!loginUser.getText().toString().equals("")
                    && !loginPassword.getText().toString().equals("")) {

                InfoTask task = new InfoTask();
                Map<String, String> params = new HashMap<>();

                userName = loginUser.getText().toString();
                password = loginPassword.getText().toString();

                params.put("mode", "LOGIN");
                params.put("username", userName);
                params.put("password", password);

                try {

                    task.execute(params);
                    String s = task.get();

                    Log.d(TAG, "!!!" + s);

                    if ((userId = Integer.parseInt(s)) != -1) {

                        // Todo refactor. Very bad code!
                        PreLoginActivity.language = language;
                        PreLoginActivity.userId = userId;
                        PreLoginActivity.userName = userName;
                        PreLoginActivity.password = password;

                        checkPermissions();
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
    }

    private void checkPermissions() {

        List<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

            Log.d(TAG, "onCreate: FINE DENIED!");

        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            permissions.add(Manifest.permission.CAMERA);

            Log.d(TAG, "onCreate: CAMERA denied");
        }

        if (permissions.isEmpty()) {
            Log.d(TAG, "onCreate: FINE OK");
            startMain();
        } else {
            PermissionDialog dialog = new PermissionDialog(REQUEST_ALL_RESULT, permissions);
            dialog.show(getActivity().getSupportFragmentManager(), "permissions");
        }
    }

    // Todo refactor this copy-paste. Start in activity.
    private void startMain() {

        Intent intent = new Intent(getActivity(), MainActivity.class);

        intent.putExtra("uid", "login");

        // TODO Send password safety
        intent.putExtra("language", language);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("password", password);

        startActivity(intent);
    }


}