package com.sibdever.algo_android.fragments.bottom;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sibdever.algo_android.MainViewModel;
import com.sibdever.algo_android.R;
import com.sibdever.algo_android.adapters.PointRecycleAdapter;
import com.sibdever.algo_android.api.commands.Command;
import com.sibdever.algo_android.api.commands.InfoCommand;
import com.sibdever.algo_android.api.commands.PictureCommand;
import com.sibdever.algo_android.api.tasks.InfoTask;
import com.sibdever.algo_android.api.tasks.PictureListTask;
import com.sibdever.algo_android.data.QuestStatus;
import com.sibdever.algo_android.data.ShortPoint;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PointsQueueFragment extends Fragment {

    private static final String TAG = "Borlehandro";

    private MainViewModel viewModel;
    private NavController controller;

    private String questId, language;

    private RecyclerView pointsQueueView;
    private Button questGoButton;
    private TextView title;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_points_queue, container, false);

        pointsQueueView = v.findViewById(R.id.pointsQueueView);
        pointsQueueView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pointsQueueView.setHasFixedSize(true);

        questGoButton = v.findViewById(R.id.questGoButton);

        progressBar = v.findViewById(R.id.pointQueueProgress);

        title = v.findViewById(R.id.pointsQueueText);

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

        language = getResources().getConfiguration().getLocales().get(0).getLanguage();
        Log.w(TAG, "onActivityCreated: " + language);

        viewModel.setQueueOpened(true);

        viewModel.getQuestFinished().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean)
                controller.navigate(R.id.toBegin);
        });

        Log.d(TAG, "onActivityCreated: queue get lang: " + language + " questId: " + questId);

    }

    @Override
    public void onStart() {

        progressBar.setVisibility(View.VISIBLE);
        pointsQueueView.setVisibility(View.INVISIBLE);
        questGoButton.setVisibility(View.INVISIBLE);

        language = getResources().getConfiguration().getLocales().get(0).getLanguage();

        title.setText(getResources().getString(R.string.pointsListHeader));
        questGoButton.setText(getResources().getString(R.string.pointQueueButton));

        InfoTask queueTask = new InfoTask(result -> {
            try {

                Log.d(TAG, "Activity get " + result);

                LinkedList<ShortPoint> queue = ShortPoint.listOf(result, language);

                // Send points to show in the map

                if(viewModel.getQuestStarted().getValue()==null || !viewModel.getQuestStarted().getValue()) {
                    viewModel.setPointsQueue(queue);
                    viewModel.setBottomSheetState(MainViewModel.BottomStates.POINTS_QUEUE_IN_PROCESS);
                }

                List<String> pointsNames = queue.stream().map(ShortPoint::getName)
                        .collect(Collectors.toList());

                // Todo CAN WE ADD SHORT POINT DESCRIPTION?

                Log.d(TAG, "onCreate. Points names: ");
                for (String item : pointsNames) {
                    Log.d(TAG, item);
                }

                // For all names download image and insert into RecyclerView
                List<PictureCommand> commands = new ArrayList<>();
                queue.stream().map(ShortPoint::getPictureName).forEach(pictureName -> {

                    Log.d(TAG, "Prepare pic name: " + pictureName);

                    PictureCommand command = PictureCommand.builder(Command.CommandType.GET_POINT_PICTURE)
                            .param("picName", pictureName)
                            .param("ticket", getActivity()
                                    .getSharedPreferences("User", Context.MODE_PRIVATE)
                                    .getString("ticket", "0"))
                            .build();

                    commands.add(command);
                });

                // Log.d(TAG, "DownloadPictureList commands: " );
                commands.forEach(item -> Log.w(TAG, "Warning: " + item.getArguments().get("picName")));

                PictureListTask pictureListTask = new PictureListTask(bitmaps -> {
                    PointRecycleAdapter adapter = new PointRecycleAdapter(pointsNames.size(), pointsNames, bitmaps);

                    pointsQueueView.setAdapter(adapter);

                    progressBar.setVisibility(View.INVISIBLE);
                    pointsQueueView.setVisibility(View.VISIBLE);
                    questGoButton.setVisibility(View.VISIBLE);

                });

                pictureListTask.execute(commands.toArray(new PictureCommand[0]));

                questGoButton.setOnClickListener((View v) -> {

                    Log.d(TAG, "onStart: IT'S TIME TO START!!! ");

                    // Quest start here!
                    // viewModel.setPointsQueue(queue);

                    InfoTask questStartTask = new InfoTask(res -> {
                        Log.d(TAG, "onStart: Start quest with res: " + res);
                        // Parse Status
                        try {
                            QuestStatus status = QuestStatus.valueOf(res, language);
                            Log.d(TAG, "onStart: " + status.getStatus());
                            viewModel.setNextPoint(status.getPoint());
                            viewModel.setQuestStarted(true);
                        } catch (JSONException e) {
                            // Todo: Say we can not start quest.
                            e.printStackTrace();
                        }

                    });

                    InfoCommand command = InfoCommand.builder(Command.CommandType.START_QUEST)
                            .param("ticket", getActivity().getSharedPreferences("User", Context.MODE_PRIVATE).getString("ticket", "0"))
                            .param("questId", questId)
                            .build();

                    questStartTask.execute(command);

                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


        InfoCommand command = InfoCommand.builder(Command.CommandType.GET_POINTS_QUEUE)
                .param("questId", questId)
                .param("ticket", getActivity()
                        .getSharedPreferences("User", Context.MODE_PRIVATE)
                        .getString("ticket", "0"))
                .param("language", language)
                .build();

        queueTask.execute(command);

        super.onStart();
    }
}