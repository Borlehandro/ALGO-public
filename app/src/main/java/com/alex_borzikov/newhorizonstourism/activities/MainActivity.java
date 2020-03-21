package com.alex_borzikov.newhorizonstourism.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.alex_borzikov.newhorizonstourism.MainViewModel;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
import com.alex_borzikov.newhorizonstourism.data.UserInfo;
import com.alex_borzikov.newhorizonstourism.dialogs.FinishDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import static com.yandex.runtime.Runtime.getApplicationContext;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";

    private UserInfo userInfo;

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

        // TODO Send password safety
        userInfo = new UserInfo(
                getIntent().getIntExtra("userId", 0),
                getIntent().getStringExtra("userName"),
                getIntent().getStringExtra("password"),
                getIntent().getStringExtra("language")
        );

        Log.d(TAG, "user " + userInfo.getName());
        Log.d(TAG, "id " + userInfo.getId());
        Log.d(TAG, "password " + userInfo.getPassword());
        Log.d(TAG, "lang " + userInfo.getLanguage());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.setUserInfo(userInfo);
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
    }

    @Override
    protected void onResumeFragments() {

        Log.d(TAG, "onResumeFragments: ");

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d(TAG, "onStateChanged: " + newState);

                if(newState==4) {
                    viewModel.setShowOpened(false);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

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

            InfoTask codeTask = new InfoTask();

            Map<String, String> codeParams = new HashMap<>();
            codeParams.put("mode", "CHECK_CODE");
            codeParams.put("code", pointCode);

            codeTask.execute(codeParams);

            try {
                pointId = codeTask.get();
                Log.d(TAG, "onRestart: get pointId: " + pointId);

                if (Integer.parseInt(pointId) == viewModel.getPointsQueue().getValue()
                        .peek().getId()) {

                    Intent toPoint = new Intent(getApplicationContext(), PointActivity.class);
                    toPoint.putExtra("pointId", pointId);
                    toPoint.putExtra("language", userInfo.getLanguage());
                    toPoint.putExtra("userName", userInfo.getName());
                    toPoint.putExtra("password", userInfo.getPassword());
                    // It can not repeat!
                    pointCode = null;
                    startActivityForResult(toPoint, 1);
                }

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: in main " + requestCode + " res: " + resultCode);

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