package com.sibdever.algo_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sibdever.algo_android.R;
import com.sibdever.algo_android.api.commands.Command;
import com.sibdever.algo_android.api.commands.InfoCommand;
import com.sibdever.algo_android.api.tasks.InfoTask;
import com.sibdever.algo_android.data.UserInfo;

import org.json.JSONException;

import java.util.Locale;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";

    private SharedPreferences preferences;

    private String userTicket;

    TextView profileTitle, questsCollectedText, questsCollectedTitle, bonusesCollectedTitle,
            bonusesCollectedText, pointCollectedText, pointCollectedTitle, kilometersCompletedText,
            kilometersCompletedTitle;

    ProgressBar progressBar;

    Button exitButton;

    RadioGroup languageGroup;

    RadioButton englishButton, russianButton, chineseButton;

    CardView cardView;

    private Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        preferences = getSharedPreferences("User", MODE_PRIVATE);

        // Not password but ticket

        Log.w(TAG, "onCreate userProfile " + preferences.getString("language", null));


        progressBar = findViewById(R.id.progressBar2);


        profileTitle = findViewById(R.id.userProfileTitle);

        questsCollectedText = findViewById(R.id.questCompletedText);
        questsCollectedTitle = findViewById(R.id.questsCompletedTitle);

        pointCollectedText = findViewById(R.id.pointsCompletedText);
        pointCollectedTitle = findViewById(R.id.pointsCompletedTitle);

        kilometersCompletedText = findViewById(R.id.kilometersCompletedText);
        kilometersCompletedTitle = findViewById(R.id.kilometresCompletedTitle);

        bonusesCollectedText = findViewById(R.id.bonusesCollectedText);
        bonusesCollectedTitle = findViewById(R.id.bonusesTitle);

        exitButton = findViewById(R.id.exitButton);

        cardView = findViewById(R.id.cardView);

        languageGroup = findViewById(R.id.changeLanguageGroup);

        englishButton = findViewById(R.id.changeEnglishButton);
        russianButton = findViewById(R.id.changeRussianButton);
        chineseButton = findViewById(R.id.changeChineseButton);

        progressBar.setVisibility(View.VISIBLE);
        profileTitle.setVisibility(View.INVISIBLE);
        questsCollectedText.setVisibility(View.INVISIBLE);
        questsCollectedTitle.setVisibility(View.INVISIBLE);
        pointCollectedText.setVisibility(View.INVISIBLE);
        pointCollectedTitle.setVisibility(View.INVISIBLE);
        kilometersCompletedText.setVisibility(View.INVISIBLE);
        kilometersCompletedTitle.setVisibility(View.INVISIBLE);
        bonusesCollectedText.setVisibility(View.INVISIBLE);
        bonusesCollectedTitle.setVisibility(View.INVISIBLE);
        exitButton.setVisibility(View.INVISIBLE);
        cardView.setVisibility(View.INVISIBLE);

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

        restartApp();
    }

    @Override
    protected void onStart() {

        Log.w(TAG, "onCreate: task " + userTicket);

        InfoTask userInfo = new InfoTask(result -> {

            try {

                Log.w(TAG, "Result: " + result);

                UserInfo info = UserInfo.valueOf(result);

                profileTitle.setText(getString(R.string.helloText, info.getName()));
                bonusesCollectedText.setText(String.valueOf(info.getBonuses()));
                questsCollectedText.setText(String.valueOf(info.getQuestsPassed()));
                pointCollectedText.setText(String.valueOf(info.getPointsPassed()));
                kilometersCompletedText.setText(String.valueOf(info.getKilometersCompleted()));

                progressBar.setVisibility(View.INVISIBLE);
                profileTitle.setVisibility(View.VISIBLE);
                questsCollectedText.setVisibility(View.VISIBLE);
                questsCollectedTitle.setVisibility(View.VISIBLE);
                pointCollectedText.setVisibility(View.VISIBLE);
                pointCollectedTitle.setVisibility(View.VISIBLE);
                kilometersCompletedText.setVisibility(View.VISIBLE);
                kilometersCompletedTitle.setVisibility(View.VISIBLE);
                bonusesCollectedText.setVisibility(View.VISIBLE);
                bonusesCollectedTitle.setVisibility(View.VISIBLE);
                exitButton.setVisibility(View.VISIBLE);
                languageGroup.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        InfoCommand command = InfoCommand.builder(Command.CommandType.USER_INFO)
                .param("ticket", userTicket)
                .build();

        userInfo.execute(command);

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
        startActivity(new Intent(this, UserProfileActivity.class));
    }

}
