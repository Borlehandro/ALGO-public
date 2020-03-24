package com.alex_borzikov.newhorizonstourism.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.alex_borzikov.newhorizonstourism.R;

public class UserProfileActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    TextView profileTitle, questsCollectedText, bonusesCollectedText,
            pointCollectedText, kilometersCompletedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        preferences = getSharedPreferences("User", MODE_PRIVATE);

        // Not password but ticket

        if(preferences.contains("user") && preferences.contains("ticket")) {

            profileTitle = findViewById(R.id.userProfileTitle);
            questsCollectedText = findViewById(R.id.questCompletedText);
            pointCollectedText = findViewById(R.id.pointsCompletedText);
            kilometersCompletedText = findViewById(R.id.kilometersCompletedText);
            bonusesCollectedText = findViewById(R.id.bonusesCollectedText);

            // Snd task to check

        }

        // Send info task to get this data

    }
}
