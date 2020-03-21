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
import android.widget.ListView;

import com.alex_borzikov.newhorizonstourism.MainViewModel;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.adapters.PointsQueueAdapter;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.api.PictureTask;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class PointsQueueFragment extends Fragment {

    private static final String TAG = "Borlehandro";

    private MainViewModel viewModel;
    private NavController controller;

    private String questId, language;

    private ListView pointsQueueView;
    private Button questGoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_points_queue, container, false);

        pointsQueueView = v.findViewById(R.id.pointsQueueView);
        questGoButton = v.findViewById(R.id.questGoButton);

        return v;
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

        Log.d(TAG, "onActivityCreated: queue get lang: " + language + " questId: " + questId);

    }

    @Override
    public void onStart() {
        Map<String, String> codeParams = new HashMap<>();
        codeParams.put("mode", "GET_POINTS_QUEUE");
        codeParams.put("questId", questId);
        codeParams.put("language", language);

        InfoTask queueTask = new InfoTask();

        queueTask.execute(codeParams);

        try {
            String json = queueTask.get();

            Log.d(TAG, "Activity get " + json);

            LinkedList<PointInfoItem> queue = JsonParser.parsePointsQueue(json);

            viewModel.setPointsQueue(queue);
            viewModel.setNeedPointsQueue(true);

            List<String> pointsNames = queue.stream().map(PointInfoItem::getName)
                    .collect(Collectors.toList());

            // Todo CAN WE ADD SHORT POINT DESCRIPTION

            Log.d(TAG, "onCreate. Points names: ");
            for (String item : pointsNames) {
                Log.d(TAG, item);
            }

            List<Bitmap> pictures = new ArrayList<>();

            // For all names download image
            for (String name : queue.stream()
                    .map(PointInfoItem::getPictureName).collect(Collectors.toList())) {

                PictureTask pictureTask = new PictureTask();
                pictureTask.execute(name.replace("\\\\", "\\"));
                pictures.add(pictureTask.get());

            }

            PointsQueueAdapter adapter = new PointsQueueAdapter(getActivity(),
                    R.layout.points_queue_item, pointsNames, pictures);

            pointsQueueView.setAdapter(adapter);

            questGoButton.setOnClickListener((View v) -> {

                Log.d(TAG, "onStart: IT'S TIME TO START!!! " );
                viewModel.setPointsQueue(queue);
                viewModel.setQuestStarted(true);

            });

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        super.onStart();
    }
}
