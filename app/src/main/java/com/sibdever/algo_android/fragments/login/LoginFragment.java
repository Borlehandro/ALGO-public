package com.sibdever.algo_android.fragments.login;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.sibdever.algo_android.R;
import com.sibdever.algo_android.activities.MainActivity;
import com.sibdever.algo_android.api.commands.Command;
import com.sibdever.algo_android.api.commands.InfoCommand;
import com.sibdever.algo_android.api.tasks.InfoTask;
import com.sibdever.algo_android.dialogs.PermissionDialog;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    private static final String TAG = "Borlehandro";

    private static String userName;
    private static String password;

    private EditText loginUser, loginPassword;

    private Button loginButton;

    private ProgressBar progress;
    private  SharedPreferences preferences;

    private String userTicket;

    private static final int REQUEST_ALL_RESULT = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("User", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        loginUser = v.findViewById(R.id.login_user);
        loginPassword = v.findViewById(R.id.login_password);

        loginButton = v.findViewById(R.id.login_button);

        progress = v.findViewById(R.id.loginProgress);
        progress.setVisibility(View.INVISIBLE);

        loginButton.setOnClickListener(view -> {

            progress.setVisibility(View.VISIBLE);

            loginUser.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            loginPassword.setVisibility(View.INVISIBLE);

            Log.d(TAG, "Login");
            if (!loginUser.getText().toString().equals("")
                    && !loginPassword.getText().toString().equals("")) {

                InfoTask task = new InfoTask(result -> {

                    progress.setVisibility(View.INVISIBLE);

                    loginUser.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                    loginPassword.setVisibility(View.VISIBLE);

                    Log.d(TAG, "!!!" + result);

                    // Todo Use UserResponse
                    if (!(result.equals("Incorrect name") || result.equals("Incorrect password"))) {
                        userTicket = result;

                        // Todo get cookies and save in Cookies Store
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("ticket", userTicket);
                        editor.apply();

                        checkPermissions();
                    }
                });

                userName = loginUser.getText().toString();
                password = loginPassword.getText().toString();

                InfoCommand command = InfoCommand.builder(Command.CommandType.LOGIN)
                        .param("name", userName)
                        .param("password", password)
                        .build();

                task.execute(command);

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().finish();
        startActivity(intent);
    }
}