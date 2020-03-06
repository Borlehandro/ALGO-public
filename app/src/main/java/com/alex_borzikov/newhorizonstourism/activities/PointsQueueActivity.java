package com.alex_borzikov.newhorizonstourism.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PointsQueueActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_queue);

        code = getIntent().getStringExtra("code");

        InfoTask pointCodeTask = new InfoTask();
        Map<String, String> codeParams = new HashMap<>();
        codeParams.put("mode", "CHECK_CODE");
        codeParams.put("code", code);
        pointCodeTask.execute(codeParams);

        try {
            String res = pointCodeTask.get();
            Log.d(TAG, "onCreate: " + res);

            InfoTask pointInfoTask = new InfoTask();

            Map<String, String> infoParams = new HashMap<>();
            infoParams.put("mode", "CHECK_CODE");
            infoParams.put("code", code);


        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
