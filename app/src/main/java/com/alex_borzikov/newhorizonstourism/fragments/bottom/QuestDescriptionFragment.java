package com.alex_borzikov.newhorizonstourism.fragments.bottom;

import android.graphics.Bitmap;
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
import java.util.concurrent.ExecutionException;

public class QuestDescriptionFragment extends Fragment {

    private static final String TAG = "Borlehandro";

    private MainViewModel viewModel;
    private NavController controller;

    private Button startButton;
    private TextView descriptionView, nameView;
    private ImageView questImage;

    private String language, questId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quest_description, container, false);

        descriptionView = view.findViewById(R.id.questDescription);
        nameView = view.findViewById(R.id.questName);

        questImage = view.findViewById(R.id.questImage);

        startButton = view.findViewById(R.id.questPointsShowButton);

        startButton.setOnClickListener((View v) -> {
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
        language = viewModel.getUserInfo().getValue().getLanguage();

        Log.d(TAG, "onActivityCreated: desc get lang: " + language + " questId: " + questId);

    }

    @Override
    public void onStart() {

        QuestInfoItem info;

        InfoTask getQuestInfoTask = new InfoTask();
        Map<String, String> getQuestparams = new HashMap<>();

        getQuestparams.put("mode", "GET_QUEST_INFO");
        getQuestparams.put("language", language);
        getQuestparams.put("questId", questId);

        getQuestInfoTask.execute(getQuestparams);

        try {

            String result = getQuestInfoTask.get();

            Log.d(TAG, "Quest info : " + result);

            info = JsonParser.parseQuestInfo(result);

            nameView.setText(info.getName());

            PictureTask pictureTask = new PictureTask();
            pictureTask.execute(info.getPictureName().replace("\\\\", "\\"));
            Bitmap bm = pictureTask.get();

            Log.d(TAG, "onCreate: " + bm.getHeight());

            questImage.setImageBitmap(bm);

            DescriptionTask task = new DescriptionTask();
            task.execute(info.getDescriptionName().replace("\\\\", "\\"));
            String res = task.get().toString();
            descriptionView.setText(res);

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

        super.onStart();
    }
}
