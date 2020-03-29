package com.alex_borzikov.newhorizonstourism.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private final List<Boolean> completed;
    private static RecyclerViewClickListener listener;
    private static int holdersCount;

    public QuestRecycleAdapter(int number, List<String> name, List<String> description,
                               List<Boolean> completed,
                               RecyclerViewClickListener recyclerViewClickListener) {
        this.name = name;
        this.description = description;
        this.completed = completed;
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
        Holder holder = new Holder(v, context);

        holdersCount++;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.nameView.setText(name.get(position));
        holder.descriptionView.setText(description.get(position));
        if (completed.get(position)) {
            holder.questButton.setBackground(holder.context.getDrawable(R.drawable.right_arrow_grey));
            holder.questButton.setClickable(false);
            holder.holderView.setClickable(false);
            holder.completedText.setText(holder.context.getString(R.string.questUnawalibleText));
        }

        Log.d(TAG, "onBindViewHolder Description set: " + holder.descriptionView.getText());

    }

    @Override
    public int getItemCount() {
        return itemsNumber;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameView, descriptionView, completedText;
        ImageView questButton;
        View holderView;

        // Test it
        Context context;

        public Holder(@NonNull View itemView, Context context) {
            super(itemView);

            nameView = itemView.findViewById(R.id.quest_name_text);
            descriptionView = itemView.findViewById(R.id.quest_description_text);
            questButton = itemView.findViewById(R.id.lookQuestButton);
            completedText = itemView.findViewById(R.id.completedText);

            this.context = context;

            itemView.setOnClickListener(this);

            holderView = itemView;

        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: Click view");
            listener.recyclerViewListClicked(view, this.getLayoutPosition());
        }
    }
}
