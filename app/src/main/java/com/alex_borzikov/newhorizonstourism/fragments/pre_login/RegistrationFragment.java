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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RegistrationFragment extends Fragment {

    private static final String TAG = "Borlehandro";

    private static int userId;
    private static String userName;
    private static String password;

    private EditText registerUser, registerPassword, registerConfirm;

    private Button registerButton;

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

        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        registerUser = v.findViewById(R.id.register_user);
        registerPassword = v.findViewById(R.id.register_password);
        registerConfirm = v.findViewById(R.id.register_confirm);

        registerButton = v.findViewById(R.id.register_button);

        registerButton.setOnClickListener(view -> {

            Log.d(TAG, "Register");

            if (!registerUser.getText().toString().equals("")
                    && !registerPassword.getText().toString().equals("")
                    && registerConfirm.getText().toString().equals(registerPassword.getText().toString())) {

                InfoTask task = new InfoTask();
                Map<String, String> params = new HashMap<>();

                String userName = registerUser.getText().toString();
                String password = registerPassword.getText().toString();

                params.put("mode", "REGISTER");
                params.put("username", userName);
                params.put("password", password);

                params.put("language", language);

                Log.d(TAG, "Language set to :" + language);

                try {
                    if ((userId = Integer.parseInt(task.execute(params).get())) != -1) {
                        // Todo refactor. Very bad code!
                        PreLoginActivity.language = language;
                        PreLoginActivity.userId = userId;
                        PreLoginActivity.userName = userName;
                        PreLoginActivity.password = password;

                        checkPermissions();
                    }
                } catch (ExecutionException | InterruptedException e) {
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
