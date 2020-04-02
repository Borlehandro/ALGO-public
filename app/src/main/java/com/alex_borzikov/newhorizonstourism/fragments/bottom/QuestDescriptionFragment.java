package com.alex_borzikov.newhorizonstourism.fragments.bottom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alex_borzikov.newhorizonstourism.MainViewModel;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.DescriptionTask;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.api.PictureTask;
import com.alex_borzikov.newhorizonstourism.data.QuestInfoItem;

import org.json.JSONException;

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

        Map<String, String> getQuestparams = new HashMap<>();

        getQuestparams.put("mode", "GET_QUEST_INFO");
        getQuestparams.put("language", language);
        getQuestparams.put("questId", questId);

        InfoTask getQuestInfoTask = new InfoTask(result -> {
            try {

                Log.d(TAG, "Quest info : " + result);

                QuestInfoItem info = JsonParser.parseQuestInfo(result);

                nameView.setText(info.getName());

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

                    task.execute(info.getDescriptionName().replace("\\\\", "\\"));

                });

                pictureTask.execute(info.getPictureName().replace("\\\\", "\\"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        getQuestInfoTask.execute(getQuestparams);

        super.onStart();
    }
}
