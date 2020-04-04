package com.alex_borzikov.newhorizonstourism.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.alex_borzikov.newhorizonstourism.MainViewModel;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
import com.alex_borzikov.newhorizonstourism.dialogs.AboutDialog;
import com.alex_borzikov.newhorizonstourism.dialogs.FinishDialog;
import com.alex_borzikov.newhorizonstourism.dialogs.LocationDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";

    private String pointId;

    public static String pointCode;

    private Fragment bottomFragment;
    private BottomSheetBehavior sheetBehavior;

    private MainViewModel viewModel;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("User", MODE_PRIVATE);

        if (!(preferences.contains("ticket")
                && preferences.contains("language"))) {

            Intent toLogin = new Intent(this, PreLoginActivity.class);
            toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(toLogin);

        } else if (!getResources().getConfiguration().getLocales().get(0).getLanguage()
                .equals(preferences.getString("language", null))) {

            setLocale(preferences.getString("language", null));

        }

        bottomFragment = getSupportFragmentManager().findFragmentById(R.id.bottomSheetNavFragment);

        Log.d(TAG, "onCreate: ");

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
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
            if (opened)
                sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.accountItem:
                startActivity(new Intent(this, UserProfileActivity.class));
                return true;
            case R.id.itemAbout:
                AboutDialog dialog = new AboutDialog();
                dialog.show(getSupportFragmentManager(), "about");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

            // Toast.makeText(getApplicationContext(), "Get code: " + pointCode, Toast.LENGTH_LONG).show();

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
        // viewModel.setNeedPointsQueue(false);

        if (requestCode == 1 && resultCode == 1) {

            LinkedList<PointInfoItem> points = viewModel.getPointsQueue().getValue();

            if (points != null && points.size() != 1) {
                points.pop();
            } else {
                // Send update to server

                Map<String, String> codeParams = new HashMap<>();
                codeParams.put("mode", "SET_COMPLETED");
                codeParams.put("userTicket",
                        preferences.getString("ticket", "0"));
                codeParams.put("questId", viewModel.getQuestId().getValue());

                InfoTask completedTask = new InfoTask(result -> {

                    if (!result.equals("-1")) {

                        FinishDialog finish = new FinishDialog(result);
                        finish.show(getSupportFragmentManager(), "finish");

                    } else Log.e(TAG, "onActivityResult: WRONG UPDATE RESULT");

                });

                completedTask.execute(codeParams);

                viewModel.setQuestFinished(true);

            }

            viewModel.setPointsQueue(points);
        }
    }

    @Override
    protected void onResume() {
        checkLocationEnabled();
        super.onResume();
    }

    public void setLocale(String localeName) {

        Locale myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        // this.recreate();
        Intent restart = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(restart);
        finish();
    }

    private void checkLocationEnabled(){
        if (!((android.location.LocationManager) getSystemService(LOCATION_SERVICE))
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            LocationDialog dialog = new LocationDialog();
            FragmentManager manager = getSupportFragmentManager();
            Log.w(TAG, "checkLocationEnabled: " + manager.toString());
            dialog.show(manager, "location");
        }
    }

}