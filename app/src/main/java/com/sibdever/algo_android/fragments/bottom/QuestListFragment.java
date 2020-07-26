package com.sibdever.algo_android.fragments.bottom;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sibdever.algo_android.RecyclerViewClickListener;
import com.sibdever.algo_android.adapters.QuestRecycleAdapter;
import com.sibdever.algo_android.api.tasks.InfoTask;
import com.sibdever.algo_android.api.commands.QuestListCommand;
import com.sibdever.algo_android.data.Quest;
import com.sibdever.algo_android.data.QuestStatus;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuestListFragment extends Fragment implements RecyclerViewClickListener {

    private static final String TAG = "Borlehandro";

    private MainViewModel viewModel;

    private RecyclerView questList;
    private TextView title;
    private ProgressBar progress;

    private NavController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "Quest List onCreateView");

        View view = inflater.inflate(R.layout.fragment_quest_list, container, false);

        questList = view.findViewById(R.id.quest_view);
        title = view.findViewById(R.id.questText);
        progress = view.findViewById(R.id.progressBar);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        questList.setLayoutManager(manager);
        questList.setHasFixedSize(true);

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

        Log.d(TAG, "onActivityCreated: set it to " + getView());

        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

    }

    @Override
    public void onStart() {

        progress.setVisibility(View.VISIBLE);
        title.setVisibility(View.INVISIBLE);
        questList.setVisibility(View.INVISIBLE);

        viewModel.setBottomSheetState(MainViewModel.BottomStates.QUEST_LIST_STATE);

        Log.d(TAG, "Quest List onStart");
        String language = getResources().getConfiguration().getLocales().get(0).getLanguage();

        title.setText(getResources().getString(R.string.questListHeader));

        Map<String, String> questListParams = new HashMap<>();
        questListParams.put("language", language); // Unused in current server version
        questListParams.put("ticket", getActivity().getSharedPreferences("User", Context.MODE_PRIVATE)
                .getString("ticket", "0"));

        InfoTask getListTask = new InfoTask(result -> {
            try {

                Log.d(TAG, "Activity get " + result);

                List<Quest> parsingResult = Quest.listOf(result, language);

                List<String> questsNames = parsingResult.stream().map(Quest::getName)
                        .collect(Collectors.toList());

                List<String> questsDescriptions = parsingResult.stream()
                        .map(Quest::getDescriptionShort).collect(Collectors.toList());

                List<Long> questsId = parsingResult.stream().map(Quest::getId)
                        .collect(Collectors.toList());

                // Todo Change to real status (not only boolean)
                List<Boolean> completed = parsingResult.stream().map(item -> item.getStatus().equals(QuestStatus.StatusType.FINISHED))
                        .collect(Collectors.toList());

                for (String item : questsNames) {
                    Log.d(TAG, item);
                }

                RecyclerViewClickListener listener = (v, position) -> {

                    Log.d(TAG, "Click on " + position);

                    viewModel.setQuestId(String.valueOf(questsId.get(position)));

                    // Test
                    viewModel.setQuest(parsingResult.get(position));

                    viewModel.setBottomSheetState(MainViewModel.BottomStates.QUEST_DESCRIPTION_STATE);

                    controller.navigate(R.id.toDescription);

                };

                QuestRecycleAdapter adapter = new QuestRecycleAdapter(questsNames.size(), questsNames,
                        questsDescriptions, completed, listener);

                questList.setAdapter(adapter);

                progress.setVisibility(View.INVISIBLE);
                title.setVisibility(View.VISIBLE);
                questList.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        QuestListCommand command = new QuestListCommand(questListParams);

        getListTask.execute(command);

        super.onStart();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }

}