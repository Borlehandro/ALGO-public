package com.alex_borzikov.newhorizonstourism.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alex_borzikov.newhorizonstourism.MainViewModel;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
import com.alex_borzikov.newhorizonstourism.dialogs.FinishDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";

    private String pointId;

    public static String pointCode;

    private Fragment bottomFragment;
    private BottomSheetBehavior sheetBehavior;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomFragment = getSupportFragmentManager().findFragmentById(R.id.bottomSheetNavFragment);

        Log.d(TAG, "onCreate: ");

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // Are you need it?
        viewModel.setShowOpened(false);
        sheetBehavior = BottomSheetBehavior.from(bottomFragment.getView());

        viewModel.getShowOpened().observe(this, (opened) -> {
            Log.d(TAG, "onCreate: observe : " + opened);
            if (opened) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        viewModel.getQueueOpened().observe(this, opened -> {
            if(opened) {

                sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

            }
        });
    }

    @Override
    protected void onResumeFragments() {

        Log.d(TAG, "onResumeFragments: ");

        sheetBehavior.setFitToContents(false);
        sheetBehavior.setHalfExpandedRatio(0.4f);

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d(TAG, "onStateChanged: " + newState);

                if (newState == BottomSheetBehavior.STATE_HIDDEN)
                    viewModel.setShowOpened(false);

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d(TAG, "onSlide: ");

            }
        });

        super.onResumeFragments();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "On restart");
        super.onRestart();

        // Todo Make code using better
        if (pointCode != null) {

            Toast.makeText(getApplicationContext(), "Get code: " + pointCode, Toast.LENGTH_LONG).show();

            Map<String, String> codeParams = new HashMap<>();
            codeParams.put("mode", "CHECK_CODE");
            codeParams.put("code", pointCode);

            InfoTask codeTask = new InfoTask(result -> {

                pointId = result;
                Log.d(TAG, "onRestart: get pointId: " + pointId);

                if (viewModel.getPointsQueue().getValue() != null && Integer.parseInt(pointId) == viewModel.getPointsQueue().getValue()
                        .peek().getId()) {

                    Intent toPoint = new Intent(getApplicationContext(), PointActivity.class);
                    toPoint.putExtra("pointId", pointId);

                    // It can not repeat!
                    pointCode = null;
                    startActivityForResult(toPoint, 1);
                }
            });

            codeTask.execute(codeParams);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: in main " + requestCode + " res: " + resultCode);
        viewModel.setNeedPointsQueue(false);

        if (requestCode == 1 && resultCode == 1) {

            LinkedList<PointInfoItem> points = viewModel.getPointsQueue().getValue();

            if (points != null && points.size() != 1) {
                points.pop();
            } else {
                Toast.makeText(MainActivity.this, "That's all!",
                        Toast.LENGTH_LONG).show();
                viewModel.setQuestFinished(true);

                FinishDialog finish = new FinishDialog();
                finish.show(getSupportFragmentManager(), "finish");

            }

            viewModel.setPointsQueue(points);
        }
    }
}