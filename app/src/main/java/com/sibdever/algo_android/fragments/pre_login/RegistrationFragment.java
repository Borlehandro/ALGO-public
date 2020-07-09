package com.sibdever.algo_android.fragments.pre_login;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;

import com.sibdever.algo_android.R;
import com.sibdever.algo_android.activities.MainActivity;
import com.sibdever.algo_android.api.Command;
import com.sibdever.algo_android.api.InfoTask;
import com.sibdever.algo_android.dialogs.PermissionDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RegistrationFragment extends Fragment {

    private static final String TAG = "Borlehandro";

    private EditText registerUser, registerPassword, registerConfirm;

    private Button registerButton;

    private ProgressBar progressBar;

    private NavController controller;
    private String language, userTicket;

    private SharedPreferences preferences;

    static final int REQUEST_ALL_RESULT = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("User", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        registerUser = v.findViewById(R.id.register_user);
        registerPassword = v.findViewById(R.id.register_password);
        registerConfirm = v.findViewById(R.id.register_confirm);

        registerButton = v.findViewById(R.id.register_button);

        progressBar = v.findViewById(R.id.registrationProgress);

        progressBar.setVisibility(View.INVISIBLE);

        registerButton.setOnClickListener(view -> {

            Log.d(TAG, "Register");

            if (!registerUser.getText().toString().equals("")
                    && !registerPassword.getText().toString().equals("")
                    && registerConfirm.getText().toString().equals(registerPassword.getText().toString())) {

                Map<String, String> params = new HashMap<>();

                String userName = registerUser.getText().toString();
                String password = registerPassword.getText().toString();

                params.put("name", userName);
                params.put("password", password);

                Log.d(TAG, "Language set to :" + language);

                progressBar.setVisibility(View.VISIBLE);

                registerUser.setVisibility(View.INVISIBLE);
                registerPassword.setVisibility(View.INVISIBLE);
                registerConfirm.setVisibility(View.INVISIBLE);
                registerButton.setVisibility(View.INVISIBLE);

                InfoTask task = new InfoTask( result -> {

                    progressBar.setVisibility(View.INVISIBLE);

                    registerUser.setVisibility(View.VISIBLE);
                    registerPassword.setVisibility(View.VISIBLE);
                    registerConfirm.setVisibility(View.VISIBLE);
                    registerButton.setVisibility(View.VISIBLE);

                    if (!result.equals("-1")) {
                        userTicket = result;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("ticket", userTicket);
                        editor.apply();
                        checkPermissions();
                    }
                });

                Command command = Command.REGISTER;
                command.setArguments(params);
                task.execute(command);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().finish();
        startActivity(intent);
    }

}
