package com.alex_borzikov.newhorizonstourism.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class PointActivity extends AppCompatActivity {

    // Todo go to main when press back

    private static final String TAG = "Borlehandro";
    private String language;
    private String pointId, userTicket;

    private TextView descriptionView, nameView;
    private ImageView imageView;
    private Button showTaskButton;
    private ProgressBar progressBar;

    private PointInfoItem info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        nameView = findViewById(R.id.pointName);
        descriptionView = findViewById(R.id.questDescription);
        imageView = findViewById(R.id.pointImage);

        showTaskButton = findViewById(R.id.questPointsShowButton);

        progressBar = findViewById(R.id.pointProgress);

        progressBar.setVisibility(View.VISIBLE);
        descriptionView.setVisibility(View.INVISIBLE);
        nameView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        showTaskButton.setVisibility(View.INVISIBLE);

        pointId = getIntent().getStringExtra("pointId");
        language = getResources().getConfiguration().getLocales().get(0).getLanguage();
        userTicket = getIntent().getStringExtra("userTicket");

        Log.d(TAG, "onCreate: id in Point " + pointId);
        Log.d(TAG, "onCreate: lang in Point " + language);

        if (!pointId.equals("-1")) {

            Map<String, String> infoParams = new HashMap<>();
            infoParams.put("mode", "GET_POINT_INFO");
            infoParams.put("pointId", pointId);
            infoParams.put("language", language);

            InfoTask pointInfoTask = new InfoTask(result -> {

                try {

                    info = JsonParser.parsePointInfo(result);

                    nameView.setText(info.getName());

                    PictureTask pictureTask = new PictureTask(bitmapResult -> {

                        Log.d(TAG, "onCreate: " + bitmapResult.getHeight());

                        imageView.setImageBitmap(bitmapResult);

                        DescriptionTask task = new DescriptionTask(descriptionResult -> {
                            String res = descriptionResult.toString();
                            descriptionView.setText(res);

                            progressBar.setVisibility(View.INVISIBLE);
                            descriptionView.setVisibility(View.VISIBLE);
                            nameView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                            showTaskButton.setVisibility(View.VISIBLE);

                        });

                        task.execute(info.getDescriptionName().replace("\\\\", "\\"));

                    });

                    pictureTask.execute(info.getPictureName().replace("\\\\", "\\"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

            pointInfoTask.execute(infoParams);

        } else {
            Log.e(TAG, "onCreate: ERROR POINT_ID == -1");
            finish();
        }

        showTaskButton.setOnClickListener((View v) -> {

            Intent toTask = new Intent(this, TaskActivity.class);

            Log.d(TAG, "onCreate: Point: " + info.getTaskId());

            toTask.putExtra("taskId", String.valueOf(info.getTaskId()));
            toTask.putExtra("userTicket", userTicket);

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
