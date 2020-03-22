package com.alex_borzikov.newhorizonstourism.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex_borzikov.newhorizonstourism.R;

import java.util.List;

public class PointRecycleAdapter extends RecyclerView.Adapter<PointRecycleAdapter.Holder> {

    private static final String TAG = "Borlehandro";

    private int itemsNumber;
    private List<String> names;
    private List<Bitmap> bitmaps;
    private static int holdersCount;

    public PointRecycleAdapter(int number, List<String> names, List<Bitmap> pictures) {
        this.names = names;
        this.bitmaps = pictures;
        itemsNumber = number;
        holdersCount = 0;
        Log.d(TAG, "RecycleAdapter: ");
    }

    @NonNull
    @Override
    public PointRecycleAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.points_queue_item, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        PointRecycleAdapter.Holder holder = new PointRecycleAdapter.Holder(v);

        holdersCount++;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PointRecycleAdapter.Holder holder, int position) {

        holder.nameView.setText(names.get(position));
        holder.descriptionView.setImageBitmap(bitmaps.get(position));

        // Log.d(TAG, "onBindViewHolder Description set: " + holder.descriptionView.getText());

    }

    @Override
    public int getItemCount() {
        return itemsNumber;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView nameView;
        ImageView descriptionView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.pointName);
            descriptionView = itemView.findViewById(R.id.pointImage);

        }
    }

}
