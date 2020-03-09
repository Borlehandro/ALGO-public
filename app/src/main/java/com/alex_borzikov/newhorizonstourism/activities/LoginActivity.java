package com.alex_borzikov.newhorizonstourism.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoginActivity extends AppCompatActivity {

    private String language;

    private static final String TAG = "Borlehandro";

    EditText loginUser, loginPassword,
            registerUser, registerPassword, registerConfirm;

    Button loginButton, registerButton;

    public void onClick(View v) {

        // Todo save user info in cash!

        switch (v.getId()) {
            case R.id.login_button:
                Log.d(TAG, "Login");
                if (!loginUser.getText().toString().equals("")
                        && !loginPassword.getText().toString().equals("")) {

                    InfoTask task = new InfoTask();
                    Map<String, String> params = new HashMap<>();

                    String userName = loginUser.getText().toString();
                    String password = loginPassword.getText().toString();

                    params.put("mode", "LOGIN");
                    params.put("username", userName);
                    params.put("password", password);
                    int userId;
                    try {

                        task.execute(params);
                        String s = task.get();

                        Log.d(TAG, "!!!" + s);
                        if ((userId = Integer.parseInt(s)) != -1) {
                            startMain(userId, userName, password);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.register_button:

                Log.d(TAG, "Register");
                if (!registerUser.getText().toString().equals("") && !registerPassword.getText().toString().equals("")
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
                    int userId;

                    try {
                        if ((userId = Integer.parseInt(task.execute(params).get(30, TimeUnit.SECONDS))) != -1) {
                            startMain(userId, userName, password);
                        }
                    } catch (ExecutionException | InterruptedException | TimeoutException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Todo set text's languages equal lang from intent

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUser = findViewById(R.id.login_user);
        loginPassword = findViewById(R.id.login_password);

        registerUser = findViewById(R.id.register_user);
        registerPassword = findViewById(R.id.register_password);
        registerConfirm = findViewById(R.id.register_confirm);

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        // Todo Check user data in cache
        // if(password = getPass(user) {

        Intent intent = new Intent(getApplicationContext(), LanguageActivity.class);
        startActivityForResult(intent, 1);

        // }
        // else {sendLangAndData to Main}

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            language = data.getStringExtra("language");
        } else {
            Log.e(TAG, "Can't get language");
        }
    }

    private void startMain(int userId, String userName, String password) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        intent.putExtra("uid", "login");

        // TODO Send password safety
        intent.putExtra("language", language);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("password", password);

        startActivity(intent);
    }
}
