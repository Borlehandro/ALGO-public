package com.alex_borzikov.newhorizonstourism.fragments.bottom;

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

import com.alex_borzikov.newhorizonstourism.MainViewModel;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.RecyclerViewClickListener;
import com.alex_borzikov.newhorizonstourism.adapters.QuestRecycleAdapter;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.data.QuestListItem;

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
        questListParams.put("mode", "GET_QUESTS_LIST");
        questListParams.put("language", language);
        questListParams.put("userTicket", getActivity().getSharedPreferences("User", Context.MODE_PRIVATE)
                .getString("ticket", "0"));

        InfoTask getListTask = new InfoTask(result -> {
            try {

                Log.d(TAG, "Activity get " + result);

                List<QuestListItem> parsingResult = JsonParser.parseQuestList(result);

                List<String> questsNames = parsingResult.stream().map(QuestListItem::getName)
                        .collect(Collectors.toList());

                List<String> questsDescriptions = parsingResult.stream()
                        .map(QuestListItem::getDescriptionShort).collect(Collectors.toList());

                List<Integer> questsId = parsingResult.stream().map(QuestListItem::getId)
                        .collect(Collectors.toList());

                List<Boolean> completed = parsingResult.stream().map(QuestListItem::isCompleted)
                        .collect(Collectors.toList());

                for (String item : questsNames) {
                    Log.d(TAG, item);
                }

                RecyclerViewClickListener listener = (v, position) -> {

                    Log.d(TAG, "Click on " + position);

                    viewModel.setQuestId(String.valueOf(questsId.get(position)));

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

        getListTask.execute(questListParams);

        super.onStart();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }

}