package com.sibdever.algo_android.fragments.bottom;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sibdever.algo_android.MainViewModel;
import com.sibdever.algo_android.R;
import com.sibdever.algo_android.api.tasks.DescriptionTask;
import com.sibdever.algo_android.api.tasks.PictureTask;
import com.sibdever.algo_android.api.commands.QuestDescriptionCommand;
import com.sibdever.algo_android.api.commands.QuestPictureCommand;
import com.sibdever.algo_android.data.Quest;

import java.util.HashMap;
import java.util.Map;

public class QuestDescriptionFragment extends Fragment {

    private static final String TAG = "Borlehandro";

    private MainViewModel viewModel;
    private NavController controller;

    private Button startButton;
    private TextView descriptionView, nameView;
    private ImageView questImage;
    private ProgressBar progressBar;

    // Test
    private Quest quest;

    private String language, questId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quest_description, container, false);

        descriptionView = view.findViewById(R.id.questDescription);
        nameView = view.findViewById(R.id.pointName);

        questImage = view.findViewById(R.id.pointImage);

        startButton = view.findViewById(R.id.questPointsShowButton);

        progressBar = view.findViewById(R.id.questProgress);

        startButton.setOnClickListener((View v) -> {
            // viewModel.setDescriptionShown(false);
            viewModel.setBottomSheetState(MainViewModel.BottomStates.POINTS_QUEUE_START);
            controller.navigate(R.id.toPointsQueue);
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        questId = viewModel.getQuestId().getValue();

        // Test
        quest = viewModel.getQuest().getValue();
        Log.w(TAG, "onActivityCreated: QUEST: " + quest.getName());

        Log.w(TAG, "onActivityCreated: " + language);

        Log.d(TAG, "onActivityCreated: desc get lang: " + language + " questId: " + questId);

    }

    @Override
    public void onStart() {

        progressBar.setVisibility(View.VISIBLE);
        descriptionView.setVisibility(View.INVISIBLE);
        nameView.setVisibility(View.INVISIBLE);
        questImage.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);

        language = getResources().getConfiguration().getLocales().get(0).getLanguage();
        startButton.setText(getResources().getString(R.string.showQuestButton));

        nameView.setText(quest.getName());

        PictureTask pictureTask = new PictureTask(bitmapResult -> {

            Log.d(TAG, "onCreate: " + bitmapResult.getHeight());

            questImage.setImageBitmap(bitmapResult);

            DescriptionTask task = new DescriptionTask(descriptionResult -> {
                String res = descriptionResult.toString();
                descriptionView.setText(res);

                progressBar.setVisibility(View.INVISIBLE);
                descriptionView.setVisibility(View.VISIBLE);
                nameView.setVisibility(View.VISIBLE);
                questImage.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);

            });

            Map<String, String> args = new HashMap<>();
            args.put("id", String.valueOf(quest.getId()));
            args.put("language", language);

            QuestDescriptionCommand command = new QuestDescriptionCommand(args);

            task.execute(command);

        });

        Map<String, String> args = new HashMap<>();
        args.put("id", String.valueOf(quest.getId()));

        QuestPictureCommand command = new QuestPictureCommand(args);

        pictureTask.execute(command);


        super.onStart();
    }
}