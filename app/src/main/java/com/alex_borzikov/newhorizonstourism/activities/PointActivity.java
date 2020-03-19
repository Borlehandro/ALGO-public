package com.alex_borzikov.newhorizonstourism.activities;

import androidx.annotation.Nullable;
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
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.api.PictureTask;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PointActivity extends AppCompatActivity {

    // Todo go to main when press back

    private static final String TAG = "Borlehandro";
    private String language;
    private String pointId, userName, password;

    private TextView descriptionView, nameView;
    private ImageView imageView;
    private Button showTaskButton;

    private PointInfoItem info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        nameView = findViewById(R.id.questName);
        descriptionView = findViewById(R.id.questDescription);
        imageView = findViewById(R.id.questImage);

        showTaskButton = findViewById(R.id.questPointsShowButton);

        pointId = getIntent().getStringExtra("pointId");
        language = getIntent().getStringExtra("language");
        userName = getIntent().getStringExtra("userName");
        password = getIntent().getStringExtra("password");

        Log.d(TAG, "onCreate: id in Point " + pointId);
        Log.d(TAG, "onCreate: lang in Point " + language);

        if (!pointId.equals("-1")) {

            InfoTask pointInfoTask = new InfoTask();

            Map<String, String> infoParams = new HashMap<>();
            infoParams.put("mode", "GET_POINT_INFO");
            infoParams.put("pointId", pointId);
            infoParams.put("language", language);

            pointInfoTask.execute(infoParams);
            try {

                String json = pointInfoTask.get();
                info = JsonParser.parsePointInfo(json);

                nameView.setText(info.getName());

                PictureTask pictureTask = new PictureTask();
                pictureTask.execute(info.getPictureName().replace("\\\\", "\\"));
                Bitmap bm = pictureTask.get();

                Log.d(TAG, "onCreate: " + bm.getHeight());

                imageView.setImageBitmap(bm);

                DescriptionTask task = new DescriptionTask();
                task.execute(info.getDescriptionName().replace("\\\\", "\\"));
                String res = task.get().toString();
                descriptionView.setText(res);

            } catch (ExecutionException | InterruptedException | JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(TAG, "onCreate: ERROR POINT_ID == -1");
            finish();
        }

        showTaskButton.setOnClickListener((View v) -> {

            Intent toTask = new Intent(this, TaskActivity.class);

            Log.d(TAG, "onCreate: Point: " + info.getTaskId());

            toTask.putExtra("language", language);
            toTask.putExtra("taskId", String.valueOf(info.getTaskId()));
            toTask.putExtra("userName", userName);
            toTask.putExtra("password", password);

            startActivityForResult(toTask, 1);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: point " + resultCode);
        setResult(resultCode);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(0);
        Log.d(TAG, "onBackPressed");
        finish();
        super.onBackPressed();
    }
}
