package com.alex_borzikov.newhorizonstourism.fragments.bottom;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.alex_borzikov.newhorizonstourism.MainViewModel;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.activities.MainActivity;
import com.alex_borzikov.newhorizonstourism.activities.QuestActivity;
import com.alex_borzikov.newhorizonstourism.adapters.QuestListAdapter;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.data.QuestListItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class QuestListFragment extends Fragment {

    BottomSheetBehavior sheetBehavior;

    private static final String TAG = "Borlehandro";

    MainViewModel viewModel;

    ListView questList;

    NavController controller;

    public QuestListFragment() {
        // Required empty public constructor
    }

    public static QuestListFragment newInstance(String param1) {
        QuestListFragment fragment = new QuestListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_quest_list, container, false);

        questList = view.findViewById(R.id.quest_view);

        InfoTask getListTask = new InfoTask();
        Map<String, String> questListParams = new HashMap<>();
        questListParams.put("mode", "GET_QUESTS_LIST");
        questListParams.put("language", ((MainActivity)getActivity()).userInfo.getLanguage());

        getListTask.execute(questListParams);

        try {
            // TODO: Don't call get() method!

            String result = getListTask.get();

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

            //ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.quest_list_layout, questsNames);
            QuestListAdapter adapter = new QuestListAdapter(getActivity(),
                    R.layout.quest_list_layout, questsNames, questsDescriptions);

            questList.setAdapter(adapter);

            questList.setOnItemClickListener((AdapterView<?> parent, View v,
                                              int position, long id) -> {

                Log.d(TAG, "Click on " + position);

                viewModel.setQuestId(String.valueOf(questsId.get(position)));

                controller.navigate(R.id.toDescription);

//                Intent toQuestInfo = new Intent(getActivity(),
//                        QuestActivity.class);
//
//                Log.d(TAG, "onCreateView: get ID:" + questsId.get(position));
//
//                toQuestInfo.putExtra("language",((MainActivity)getActivity()).userInfo.getLanguage());
//                toQuestInfo.putExtra("questId", String.valueOf(questsId.get(position)));
//
//                startActivityForResult(toQuestInfo, 1);

            });

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        // sheetBehavior = BottomSheetBehavior.from(view);

        // Log.d(TAG, "onCreateView: " + sheetBehavior);

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

        // ok?
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        viewModel.getShowOpened().observe(getViewLifecycleOwner(), (opened) -> {
            if (opened){
                // Todo Get data and make list view!
            }
            else
                Log.d(TAG, "onActivityCreated: NOT OPEN IN BOTTOM");
        });
    }
}