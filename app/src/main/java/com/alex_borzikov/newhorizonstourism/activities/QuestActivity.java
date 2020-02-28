package com.alex_borzikov.newhorizonstourism.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.ApiClient;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.api.ServerTask;
import com.alex_borzikov.newhorizonstourism.data.QuestInfoItem;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class QuestActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";

    private String language, questId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");

        language = getIntent().getStringExtra("language");

        Log.d(TAG, "onCreate: lang = " + language);

        questId = getIntent().getStringExtra("questId");

        Log.d(TAG, "onCreate: questId = " + questId);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        ServerTask getQuestInfoTask = new ServerTask();
        Map<String, String> getQuestparams = new HashMap<>();

        getQuestparams.put("mode", "GET_QUEST_INFO");
        getQuestparams.put("language", language);
        getQuestparams.put("questId", questId);

        getQuestInfoTask.execute(getQuestparams);

        try {

            String result = getQuestInfoTask.get();

            Log.d(TAG, "Quest info : " + result);

            QuestInfoItem info = JsonParser.parseQuestInfo(result);

            // Todo get image and text from server





        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

    }
}
