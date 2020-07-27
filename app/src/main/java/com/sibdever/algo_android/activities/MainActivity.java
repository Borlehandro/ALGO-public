package com.sibdever.algo_android.activities;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.sibdever.algo_android.MainViewModel;
import com.sibdever.algo_android.R;
import com.sibdever.algo_android.api.commands.QuestFinishMessageCommand;
import com.sibdever.algo_android.api.tasks.InfoTask;
import com.sibdever.algo_android.api.commands.PointByCodeCommand;
import com.sibdever.algo_android.data.Point;
import com.sibdever.algo_android.data.QuestFinishMessage;
import com.sibdever.algo_android.data.QuestStatus;
import com.sibdever.algo_android.dialogs.AboutDialog;
import com.sibdever.algo_android.dialogs.FinishDialog;
import com.sibdever.algo_android.dialogs.LocationDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";

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
            Log.d(TAG, "onRestart: " + "Get code: " + pointCode);

            InfoTask codeTask = new InfoTask(result -> {

                Log.w(TAG, "GET RESULT IN LAMBDA: " + result);

                try {

                    Point point = Point.valueOf(result, preferences.getString("language", "en"));

                    Log.d(TAG, "onRestart: get point: " + point.getName());


                    Intent toPoint = new Intent(getApplicationContext(), PointActivity.class);
                    toPoint.putExtra("point", point);

                    // It can not repeat!
                    pointCode = null;
                    startActivityForResult(toPoint, 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            PointByCodeCommand command = PointByCodeCommand.builder()
                    .param("code", pointCode)
                    .param("ticket", preferences.getString("ticket", "0"))
                    .build();

            codeTask.execute(command);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: in main " + requestCode + " res: " + resultCode);
        // viewModel.setNeedPointsQueue(false);

        if (requestCode == 1 && resultCode == 1) {

            // Return result from task. Next ShortPoint or finish status.
            QuestStatus status = (QuestStatus) data.getSerializableExtra("questStatus");

            if (status.getStatus() != QuestStatus.StatusType.FINISHED
                    && status.getStatus() != QuestStatus.StatusType.FINISHED_AGAIN
                    && status.getStatus() != QuestStatus.StatusType.FINISHED_FIRST_TIME) {
                viewModel.setNextPoint(status.getPoint());
            } else {

                // Send update to server
                Toast.makeText(getApplicationContext(), "FINISHED", Toast.LENGTH_SHORT).show();

                QuestFinishMessageCommand command = QuestFinishMessageCommand.builder()
                        .param("ticket", preferences.getString("ticket", "0"))
                        .param("questId", viewModel.getQuestId().getValue())
                        .param("language", preferences.getString("language", "en"))
                        .build();

                InfoTask completedTask = new InfoTask(result -> {
                    try {

                        // TODO: Check correct
                        if (!result.equals("-1")) {

                            // Show finish message.
                            FinishDialog finish = new FinishDialog(QuestFinishMessage.valueOf(result));
                            finish.show(getSupportFragmentManager(), "finish");
                        } else
                            Log.e(TAG, "onActivityResult: WRONG UPDATE RESULT");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

                completedTask.execute(command);

                viewModel.setQuestFinished(true);

            }
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

    private void checkLocationEnabled() {
        if (!((android.location.LocationManager) getSystemService(LOCATION_SERVICE))
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            LocationDialog dialog = new LocationDialog();
            FragmentManager manager = getSupportFragmentManager();
            Log.w(TAG, "checkLocationEnabled: " + manager.toString());
            dialog.show(manager, "location");
        }
    }

}