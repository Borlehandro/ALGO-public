package com.sibdever.algo_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sibdever.algo_android.R;
import com.sibdever.algo_android.api.tasks.DescriptionTask;
import com.sibdever.algo_android.api.tasks.PictureTask;
import com.sibdever.algo_android.api.commands.PointDescriptionCommand;
import com.sibdever.algo_android.api.commands.PointPictureCommand;
import com.sibdever.algo_android.data.Point;
import com.sibdever.algo_android.dialogs.AboutDialog;

import java.util.HashMap;
import java.util.Map;

public class PointActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";
    private String language;
    private Point point;

    private TextView descriptionView, nameView;
    private ImageView imageView;
    private Button showTaskButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        nameView = findViewById(R.id.pointName);
        descriptionView = findViewById(R.id.questDescription);
        imageView = findViewById(R.id.pointImage);

        showTaskButton = findViewById(R.id.questPointsShowButton);

        progressBar = findViewById(R.id.pointProgress);

        point = (Point) getIntent().getSerializableExtra("point");

        Log.d(TAG, "onCreate: point in Point " + point.getName());
        Log.d(TAG, "onCreate: lang in Point " + language);

        showTaskButton.setOnClickListener((View v) -> {

            Intent toTask = new Intent(this, TaskActivity.class);

            Log.d(TAG, "onCreate: Point: " + point.getTaskId());

            toTask.putExtra("taskId", String.valueOf(point.getTaskId()));

            startActivityForResult(toTask, 1);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        language = getResources().getConfiguration().getLocales().get(0).getLanguage();
        showTaskButton.setText(getResources().getString(R.string.getPointTask));

        progressBar.setVisibility(View.VISIBLE);
        descriptionView.setVisibility(View.INVISIBLE);
        nameView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        showTaskButton.setVisibility(View.INVISIBLE);

        nameView.setText(point.getName());

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

            Map<String, String> args = new HashMap<>();
            args.put("id", String.valueOf(point.getId()));
            args.put("language", language);
            args.put("ticket", getSharedPreferences("User", MODE_PRIVATE).getString("ticket", "0"));

            PointDescriptionCommand command = new PointDescriptionCommand(args);
            task.execute(command);

        });

        Map<String, String> args = new HashMap<>();
        args.put("picName", point.getPictureName());
        args.put("ticket", getSharedPreferences("User", MODE_PRIVATE).getString("ticket", "0"));

        PointPictureCommand command = new PointPictureCommand(args);
        pictureTask.execute(command);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: point " + resultCode);
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(0);
        Log.d(TAG, "onBackPressed");
        finish();
        super.onBackPressed();
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

}
