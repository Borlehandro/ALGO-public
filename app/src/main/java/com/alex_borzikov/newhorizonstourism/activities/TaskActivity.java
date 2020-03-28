package com.alex_borzikov.newhorizonstourism.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.LogWriter;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.data.TaskInfoItem;
import com.alex_borzikov.newhorizonstourism.dialogs.AboutDialog;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TaskActivity extends AppCompatActivity {

    private static final String TAG = "Borlehandro";
    private String language;
    private String taskId, userTicket;

    private TextView descriptionTask, taskTitle;
    private RadioGroup group;
    private RadioButton choice1, choice2, choice3;
    private ImageButton checkButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        taskId = getIntent().getStringExtra("taskId");

        Log.d(TAG, "onCreate: Task: " + taskId);

        userTicket = getSharedPreferences("User", MODE_PRIVATE).getString("ticket", null);

        descriptionTask = findViewById(R.id.taskDescription);

        taskTitle = findViewById(R.id.taskTitle);

        group = findViewById(R.id.radioGroup);

        choice1 = findViewById(R.id.radio1);
        choice2 = findViewById(R.id.radio2);
        choice3 = findViewById(R.id.radio3);

        checkButton = findViewById(R.id.checkButton);

        progressBar = findViewById(R.id.taskProgress);

        checkButton.setOnClickListener((View v) -> {

            Map<String, String> params = new HashMap<>();
            params.put("mode", "CHECK_ANSWER");
            params.put("taskId", taskId);

            params.put("answerIndex", String.valueOf(group.indexOfChild(
                    group.findViewById(group.getCheckedRadioButtonId())
            ) + 1));

            Log.d(TAG, "onCreate: Answer index: " + group.indexOfChild(
                    group.findViewById(group.getCheckedRadioButtonId())
            ));

            params.put("userTicket", userTicket);

            InfoTask checkAnswer = new InfoTask(result -> {

                if (result.equals("1")) {
                    Toast.makeText(TaskActivity.this, getString(R.string.taskSuccess), Toast.LENGTH_LONG)
                            .show();

                    setResult(1);
                    finish();

                } else {
                    Toast.makeText(TaskActivity.this, getString(R.string.taskFail), Toast.LENGTH_LONG)
                            .show();
                }
            });

            checkAnswer.execute(params);

        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        language = getResources().getConfiguration().getLocales().get(0).getLanguage();
        // checkButton.setText(getResources().getString(R.string.taskButton));
        taskTitle.setText(getResources().getString(R.string.taskTitle));

        progressBar.setVisibility(View.VISIBLE);
        descriptionTask.setVisibility(View.INVISIBLE);
        group.setVisibility(View.INVISIBLE);
        checkButton.setVisibility(View.INVISIBLE);

        Map<String, String> args = new HashMap<>();
        args.put("mode", "GET_TASK_INFO");
        args.put("taskId", taskId);
        args.put("language", language);
        args.put("userTicket", userTicket);

        Log.w(TAG, "onCreate: task " + userTicket);

        InfoTask taskInfo = new InfoTask(result -> {

            try {

                Log.w(TAG, "Result: " + result);

                TaskInfoItem info = JsonParser.parseTaskInfo(result);

                descriptionTask.setText(info.getDescriptionShort());
                choice1.setText(info.getChoice1());
                choice2.setText(info.getChoice2());
                choice3.setText(info.getChoice3());

                progressBar.setVisibility(View.INVISIBLE);
                descriptionTask.setVisibility(View.VISIBLE);
                group.setVisibility(View.VISIBLE);
                checkButton.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        taskInfo.execute(args);
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
