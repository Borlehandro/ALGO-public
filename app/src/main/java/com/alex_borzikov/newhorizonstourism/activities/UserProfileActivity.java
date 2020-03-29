package com.alex_borzikov.newhorizonstourism.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.data.TaskInfoItem;
import com.alex_borzikov.newhorizonstourism.data.UserInfo;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";

    private SharedPreferences preferences;

    private String userTicket;

    TextView profileTitle, questsCollectedText, bonusesCollectedText,
            pointCollectedText, kilometersCompletedText;

    Button exitButton;

    RadioGroup languageGroup;

    RadioButton englishButton, russianButton, chineseButton;

    private Locale myLocale;

    Intent starterIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        preferences = getSharedPreferences("User", MODE_PRIVATE);

        starterIntent = getIntent();

        // Not password but ticket

        Log.w(TAG, "onCreate userProfile " + preferences.getString("language", null));

        profileTitle = findViewById(R.id.userProfileTitle);
        questsCollectedText = findViewById(R.id.questCompletedText);
        pointCollectedText = findViewById(R.id.pointsCompletedText);
        kilometersCompletedText = findViewById(R.id.kilometersCompletedText);
        bonusesCollectedText = findViewById(R.id.bonusesCollectedText);
        exitButton = findViewById(R.id.exitButton);

        languageGroup = findViewById(R.id.changeLanguageGroup);

        englishButton = findViewById(R.id.changeEnglishButton);
        russianButton = findViewById(R.id.changeRussianButton);
        chineseButton = findViewById(R.id.changeChineseButton);

        String language = preferences.getString("language", "en");

        userTicket = preferences.getString("ticket", "0");

        switch (language) {

            case "ru":
                russianButton.setChecked(true);
                break;

            case "zh":
                chineseButton.setChecked(true);
                break;

            default:
                englishButton.setChecked(true);
                break;

        }

        languageGroup.setOnCheckedChangeListener((radioGroup, i) -> {

            Log.d(TAG, "onCreate: changed " + i + " " + R.id.changeRussianButton);

            if (i == chineseButton.getId() && !language.equals("zh"))
                setLocale("zh");
            else if (i == russianButton.getId() && !language.equals("ru"))
                setLocale("ru");
            else if (!language.equals("en"))
                setLocale("en");
        });

        exitButton.setOnClickListener((view) -> {
            Intent toLogin = new Intent(this, PreLoginActivity.class);
            toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("language");
            editor.remove("ticket");
            editor.apply();
            finish();
            startActivity(toLogin);
        });

        // Send info task to get this data
    }

    public void setLocale(String localeName) {

        Log.w(TAG, "setLocale: Try to set " + localeName);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", localeName);
        editor.apply();

        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        // this.recreate();
        restartApp();
    }

    @Override
    protected void onStart() {

        Map<String, String> args = new HashMap<>();
        args.put("mode", "GET_USER_INFO");
        args.put("userTicket", userTicket);

        Log.w(TAG, "onCreate: task " + userTicket);

        InfoTask userInfo = new InfoTask(result -> {

            try {

                Log.w(TAG, "Result: " + result);

                UserInfo info = JsonParser.parseUserInfo(result);

                profileTitle.setText(getString(R.string.helloText, info.getUserName()));
                bonusesCollectedText.setText(String.valueOf(info.getBonuses()));
                questsCollectedText.setText(String.valueOf(info.getQuestsCompleted()));
                pointCollectedText.setText(String.valueOf(info.getPointsCompleted()));
                kilometersCompletedText.setText(String.valueOf(info.getKilometersCompleted()));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        userInfo.execute(args);

        super.onStart();
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed: pref:" + preferences.getString("language", null) + " res: "
                + getResources().getConfiguration().getLocales().get(0).getLanguage());

        super.onBackPressed();
    }

    public void restartApp() {
        finish();
        startActivity(starterIntent);
    }

}
