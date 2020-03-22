package com.alex_borzikov.newhorizonstourism.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alex_borzikov.newhorizonstourism.R;

import java.util.List;

public class QuestListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> names;
    private final List<String> descriptions;

    public QuestListAdapter(Context context, int id, List<String> names, List<String> descriptions) {

        super(context, id, names);

        this.context = context;
        this.names = names;
        this.descriptions = descriptions;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.quest_list_layout, null, true);

        TextView nameView = item.findViewById(R.id.quest_name_text);
        TextView descriptionView = item.findViewById(R.id.quest_description_text);

        Log.d("Borlehandro", "onStart DESCRIPTIONS " + position + " : " + descriptions.get(position));

        nameView.setText(names.get(position));
        descriptionView.setText(descriptions.get(position));

        return item;
    }
}
