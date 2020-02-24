package com.alex_borzikov.newhorizonstourism.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alex_borzikov.newhorizonstourism.R;

public class LanguageActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
    }

    public void onClick(View v) {

        // Todo save language info in cash!

        String language;

        switch (v.getId()){

            case R.id.englishButton:
                language = "english";
                break;
            case R.id.chineseButton:
                language = "chinese";
                break;
            case R.id.russianButton:
                language = "russian";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

        Intent intent = new Intent();
        intent.putExtra("language", language);
        setResult(RESULT_OK, intent);
        finish();
    }
}
