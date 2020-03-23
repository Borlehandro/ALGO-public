package com.alex_borzikov.newhorizonstourism.fragments.bottom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private NavController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quest_list, container, false);

        questList = view.findViewById(R.id.quest_view);

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

        Map<String, String> questListParams = new HashMap<>();
        questListParams.put("mode", "GET_QUESTS_LIST");
        questListParams.put("language", viewModel.getUserInfo().getValue().getLanguage());

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

                for (String item : questsNames) {
                    Log.d(TAG, item);
                }

                RecyclerViewClickListener listener = (v, position) -> {

                    Log.d(TAG, "Click on " + position);

                    viewModel.setQuestId(String.valueOf(questsId.get(position)));

                    controller.navigate(R.id.toDescription);

                };

                QuestRecycleAdapter adapter = new QuestRecycleAdapter(questsNames.size(), questsNames,
                        questsDescriptions, listener);

                questList.setAdapter(adapter);

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