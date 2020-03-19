package com.alex_borzikov.newhorizonstourism.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alex_borzikov.newhorizonstourism.R;

import java.util.List;

public class PointsQueueAdapter extends ArrayAdapter<String> {


    private final Context context;
    private final List<String> names;
    private final List<Bitmap> pictures;

    public PointsQueueAdapter(Context context, int id, List<String> names, List<Bitmap> pictures) {

        super(context, id, names);

        this.context = context;
        this.names = names;
        this.pictures = pictures;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.points_queue_item, null, true);

        TextView nameView = item.findViewById(R.id.questName);
        ImageView imageView = item.findViewById(R.id.questImage);

        nameView.setText(names.get(position));
        imageView.setImageBitmap(pictures.get(position));

        return item;
    }



}
