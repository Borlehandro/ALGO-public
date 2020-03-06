package com.alex_borzikov.newhorizonstourism.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
    private String pointId;

    TextView descriptionView, nameView;
    ImageView imageView;
    Button showTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        nameView = findViewById(R.id.pointName);
        descriptionView = findViewById(R.id.pointDescription);
        imageView = findViewById(R.id.pointImage);

        showTaskButton = findViewById(R.id.pointTaskShowButton);

        pointId = getIntent().getStringExtra("pointId");
        language = getIntent().getStringExtra("language");

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
                PointInfoItem info = JsonParser.parsePointInfo(json);

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
    }
}
