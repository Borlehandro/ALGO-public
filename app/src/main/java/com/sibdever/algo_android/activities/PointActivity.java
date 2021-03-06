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
import com.sibdever.algo_android.api.commands.Command;
import com.sibdever.algo_android.api.commands.DescriptionCommand;
import com.sibdever.algo_android.api.commands.PictureCommand;
import com.sibdever.algo_android.api.tasks.DescriptionTask;
import com.sibdever.algo_android.api.tasks.PictureTask;
import com.sibdever.algo_android.data.Point;
import com.sibdever.algo_android.dialogs.AboutDialog;

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

            // PointDescriptionCommand
            DescriptionCommand command = DescriptionCommand.builder(Command.CommandType.GET_POINT_DESCRIPTION)
                    .param("id", String.valueOf(point.getId()))
                    .param("language", language)
                    .param("ticket", getSharedPreferences("User", MODE_PRIVATE).getString("ticket", "0"))
                    .build();
            task.execute(command);

        });

        // PointPictureCommand
        PictureCommand command = PictureCommand.builder(Command.CommandType.GET_POINT_PICTURE)
                .param("picName", point.getPictureName())
                .param("ticket", getSharedPreferences("User", MODE_PRIVATE).getString("ticket", "0"))
                .build();

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
