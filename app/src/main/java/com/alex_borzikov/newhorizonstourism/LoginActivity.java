package com.alex_borzikov.newhorizonstourism;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alex_borzikov.newhorizonstourism.API.ConnectionModes;
import com.alex_borzikov.newhorizonstourism.API.ServerTask;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";

    EditText loginUser, loginPassword,
            registerUser, registerPassword, registerConfirm;

    Button loginButton, registerButton;

    RadioGroup languageGroup;

    RadioButton englishRadio, russianRadio, chineseRadio;

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                Log.d(TAG, "Login");
                if (!loginUser.getText().toString().equals("") && !loginPassword.getText().toString().equals("")) {
                    ServerTask task = new ServerTask();
                    Map<String, String> params = new HashMap<>();
                    params.put("mode", "LOGIN");
                    params.put("username", loginUser.getText().toString());
                    params.put("password", loginPassword.getText().toString());
                    task.execute(params);
                }
                break;

            case R.id.register_button:

                Log.d(TAG, "Register");
                if (!registerUser.getText().toString().equals("") && !registerPassword.getText().toString().equals("")
                        && registerConfirm.getText().toString().equals(registerPassword.getText().toString())) {

                    ServerTask task = new ServerTask();
                    Map<String, String> params = new HashMap<>();
                    params.put("mode", "REGISTER");
                    params.put("username", registerUser.getText().toString());
                    params.put("password", registerPassword.getText().toString());

                    if (englishRadio.isChecked())
                        params.put("language", "english");
                    else if (russianRadio.isChecked())
                        params.put("language", "russian");
                    else
                        params.put("language", "chinese");

                    task.execute(params);
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUser = findViewById(R.id.login_user);
        loginPassword = findViewById(R.id.login_password);

        registerUser = findViewById(R.id.register_user);
        registerPassword = findViewById(R.id.register_password);
        registerConfirm = findViewById(R.id.register_confirm);

        languageGroup = findViewById(R.id.languages_group);

        englishRadio = findViewById(R.id.english_radio);
        russianRadio = findViewById(R.id.russian_radio);
        chineseRadio = findViewById(R.id.chinese_radio);

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
    }
}
