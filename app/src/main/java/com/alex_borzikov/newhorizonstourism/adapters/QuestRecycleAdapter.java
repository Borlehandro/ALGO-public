package com.alex_borzikov.newhorizonstourism.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.RecyclerViewClickListener;

import java.util.List;

public class QuestRecycleAdapter extends RecyclerView.Adapter<QuestRecycleAdapter.Holder> {

    private static final String TAG = "Borlehandro";

    private int itemsNumber;
    private List<String> name, description;
    private static RecyclerViewClickListener listener;
    private static int holdersCount;

    public QuestRecycleAdapter(int number, List<String> name, List<String> description, RecyclerViewClickListener recyclerViewClickListener) {
        this.name = name;
        this.description = description;
        itemsNumber = number;
        holdersCount = 0;
        listener = recyclerViewClickListener;
        Log.d(TAG, "RecycleAdapter: ");
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.quest_list_layout, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        Holder holder = new Holder(v);

        holdersCount++;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.nameView.setText(name.get(position));
        holder.descriptionView.setText(description.get(position));
        Log.d(TAG, "onBindViewHolder Description set: " + holder.descriptionView.getText());

    }

    @Override
    public int getItemCount() {
        return itemsNumber;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameView, descriptionView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.quest_name_text);
            descriptionView = itemView.findViewById(R.id.quest_description_text);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: Click view");
            listener.recyclerViewListClicked(view, this.getLayoutPosition());
        }
    }
}
