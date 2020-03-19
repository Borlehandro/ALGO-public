package com.alex_borzikov.newhorizonstourism.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";

    // Todo make it private
    public UserInfo userInfo;

    String pointId;

    public static String pointCode;
    public static LinkedList<PointInfoItem> currentPointsQueue;

    Fragment bottomFragment;
    BottomSheetBehavior sheetBehavior;

    MainViewModel viewModel;
    boolean bottomOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomFragment = getSupportFragmentManager().findFragmentById(R.id.bottomSheetNavFragment);

        Log.d(TAG, "onCreate: ");

        // Check it is movable
//            if(mapView.getScreenshot()==null)
//                Log.w(TAG, "onCreate: IT'S NOT MOVABLE");
//            else Log.w(TAG, "onCreate: IT'S MOVABLE");

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.main_layout, fragment);
//        fragmentTransaction.commit();

        Log.w(TAG, "Shown!");

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

        viewModel.getQuestStarted().observe(this, (started) -> {
            viewModel.setShowOpened(false);
            Log.d(TAG, "onCreate: FIRST POINT IN "
                    + viewModel.getPointsQueue().getValue().get(0).getLocationX() + ";"
                    + viewModel.getPointsQueue().getValue().get(0).getLocationY());
            // Todo draw way to the first point
        });

    }

    @Override
    protected void onResumeFragments() {

        Log.d(TAG, "onResumeFragments: ");
        Log.d(TAG, "onResumeFragments: " + bottomFragment.getView());

        // Todo Hey, can you send it to map fragment?!

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d(TAG, "onStateChanged: " + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        if (bottomOpened) {
            // Todo EXIST HALF_EXPANDED - CHECK IT!
            Log.d(TAG, "onResumeFragments: set expanded");
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            Log.d(TAG, "onResumeFragments: set hidden");
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }


        super.onResumeFragments();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "On restart");
        super.onRestart();

        // Todo It's not onRestart()
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
                // Todo check pointId == currentQuestPoint
                Intent toPoint = new Intent(getApplicationContext(), PointActivity.class);
                toPoint.putExtra("pointId", pointId);
                toPoint.putExtra("language", userInfo.getLanguage());
                // It can not repeat!
                pointCode = null;
                startActivity(toPoint);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    public static class QuestListFragment extends Fragment {
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//
//            Log.d(TAG, "onCreateView: ");
//
//            View layout = inflater.inflate(R.layout.activity_main, container, false);
//
//            // Inflate the layout for this fragment
//
//            return layout;
//        }
//    }

    /**
     * Interesting code for animation
     */

    // slide the view from below itself to the current position
    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,  // fromXDelta
                0,  // toXDelta
                view.getHeight(),  // fromYDelta
                0);  // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,  // fromXDelta
                0,  // toXDelta
                0,  // fromYDelta
                view.getHeight());  // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
}