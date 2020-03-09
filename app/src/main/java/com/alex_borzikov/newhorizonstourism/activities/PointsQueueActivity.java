package com.alex_borzikov.newhorizonstourism.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.adapters.PointsQueueAdapter;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.api.PictureTask;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class PointsQueueActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";
    private String questId, language;

    ListView pointsQueueView;
    Button questGoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_queue);

        pointsQueueView = findViewById(R.id.pointsQueueView);
        questGoButton = findViewById(R.id.questGoButton);

        questId = getIntent().getStringExtra("questId");
        language = getIntent().getStringExtra("language");

        Map<String, String> codeParams = new HashMap<>();
        codeParams.put("mode", "GET_POINTS_QUEUE");
        codeParams.put("questId", questId);
        codeParams.put("language", language);

        InfoTask queueTask = new InfoTask();

        queueTask.execute(codeParams);

        try {
            String json = queueTask.get();

            Log.d(TAG, "Activity get " + json);

            List<PointInfoItem> queue = JsonParser.parsePointsQueue(json);

            List<String> pointsNames = queue.stream().map(PointInfoItem::getName)
                    .collect(Collectors.toList());

            // Todo CAN WE ADD SHORT POINT DESCRIPTION

            Log.d(TAG, "onCreate. Points names: ");
            for (String item : pointsNames) {
                Log.d(TAG, item);
            }


            List<Bitmap> pictures = new ArrayList<>();

            // For all names download image
            for (String name : queue.stream()
                    .map(PointInfoItem::getPictureName).collect(Collectors.toList())) {

                PictureTask pictureTask = new PictureTask();
                pictureTask.execute(name.replace("\\\\", "\\"));
                pictures.add(pictureTask.get());

            }

           PointsQueueAdapter adapter = new PointsQueueAdapter(getApplicationContext(),
                    R.layout.points_queue_item, pointsNames, pictures);

           pointsQueueView.setAdapter(adapter);

            questGoButton.setOnClickListener((View v) -> {

                Intent toMain = new Intent(this, MainActivity.class);

                toMain.putExtra("language", language);
                toMain.putExtra("questId", questId);

                toMain.putExtra("uid", "queue");

                // Todo Fix it, it's bad idea!

                MainActivity.currentPointsQueue = queue;

                startActivity(toMain);

            });



        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

    }
}
