package com.sibdever.algo_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sibdever.algo_android.R;
import com.sibdever.algo_android.api.commands.Command;
import com.sibdever.algo_android.api.commands.InfoCommand;
import com.sibdever.algo_android.api.tasks.InfoTask;
import com.sibdever.algo_android.data.QuestStatus;
import com.sibdever.algo_android.data.Task;
import com.sibdever.algo_android.dialogs.AboutDialog;

import org.json.JSONException;

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


            Log.d(TAG, "onCreate: Answer index: " + group.indexOfChild(
                    group.findViewById(group.getCheckedRadioButtonId())
            ));

            InfoTask checkAnswer = new InfoTask(result -> {
                try {

                    QuestStatus status = QuestStatus.valueOf(result, language);

                    if(!status.getStatus().equals(QuestStatus.StatusType.NOT_CHANGED)) {

                        // OK
                        Toast.makeText(TaskActivity.this, getString(R.string.taskSuccess), Toast.LENGTH_SHORT)
                                .show();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("questStatus", status);

                        setResult(1, resultIntent);

                        finish();

                    } else {

                        // NOT OK
                        Toast.makeText(TaskActivity.this, getString(R.string.taskFail), Toast.LENGTH_SHORT)
                                .show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            InfoCommand command = InfoCommand.builder(Command.CommandType.CHECK_TASK)
                    .param("taskId", taskId)
                    .param("variant", String.valueOf(group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId())) + 1))
                    .param("ticket", userTicket)
                    .build();

            checkAnswer.execute(command);

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

        Log.w(TAG, "onCreate: task " + userTicket);

        InfoTask taskInfo = new InfoTask(result -> {

            try {

                Log.w(TAG, "Result: " + result);

                Task info = Task.valueOf(result, language);

                descriptionTask.setText(info.getDescription());
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

        // TaskInfoCommand
        InfoCommand command = InfoCommand.builder(Command.CommandType.GET_TASK_INFO)
                .param("taskId", taskId)
                .param("language", language)
                .param("ticket", userTicket)
                .build();

        taskInfo.execute(command);
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
