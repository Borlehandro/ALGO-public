package com.alex_borzikov.newhorizonstourism.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.DescriptionTask;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.api.PictureTask;
import com.alex_borzikov.newhorizonstourism.data.QuestInfoItem;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class QuestActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";

    private String language, questId;
    ImageView image;
    TextView description, questName;
    Button questShowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        image = findViewById(R.id.pointImage);
        description = findViewById(R.id.pointDescription);
        questName = findViewById(R.id.pointName);
        questShowButton = findViewById(R.id.pointTaskShowButton);

        Log.d(TAG, "onCreate");

        language = getIntent().getStringExtra("language");

        Log.d(TAG, "onCreate: lang = " + language);

        questId = getIntent().getStringExtra("questId");

        Log.d(TAG, "onCreate: questId = " + questId);

        QuestInfoItem info;

        InfoTask getQuestInfoTask = new InfoTask();
        Map<String, String> getQuestparams = new HashMap<>();

        getQuestparams.put("mode", "GET_QUEST_INFO");
        getQuestparams.put("language", language);
        getQuestparams.put("questId", questId);

        getQuestInfoTask.execute(getQuestparams);

        try {

            String result = getQuestInfoTask.get();

            Log.d(TAG, "Quest info : " + result);

            info = JsonParser.parseQuestInfo(result);

            questName.setText(info.getName());

            PictureTask pictureTask = new PictureTask();
            pictureTask.execute(info.getPictureName().replace("\\\\", "\\"));
            Bitmap bm = pictureTask.get();

            Log.d(TAG, "onCreate: " + bm.getHeight());

            image.setImageBitmap(bm);

            DescriptionTask task = new DescriptionTask();
            task.execute(info.getDescriptionName().replace("\\\\", "\\"));
            String res = task.get().toString();
            description.setText(res);

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

        questShowButton.setOnClickListener((View v)-> {

            Intent toPointsQueue = new Intent(this, PointsQueueActivity.class);
            toPointsQueue.putExtra("questId", questId);
            toPointsQueue.putExtra("language", language);
            startActivity(toPointsQueue);

        });

    }
}
