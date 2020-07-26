package com.sibdever.algo_android.api.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.sibdever.algo_android.api.commands.PictureCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PictureListTask extends AsyncTask<PictureCommand, Integer, List<Bitmap>> {

    private final Consumer<List<Bitmap>> listDrawer;

    public PictureListTask(Consumer<List<Bitmap>> listDrawer) {
        this.listDrawer = listDrawer;
    }

    @Override
    protected List<Bitmap> doInBackground(PictureCommand... commands) {
        List<Bitmap> resultList = new ArrayList<>();
        for(int i = 0; i < commands.length; ++i) {
            Log.d("Borlehandro", "doInBackground: " + commands[i].getArguments().get("picName"));
            resultList.add(commands[i].execute());
            publishProgress((int) ((i / (double)commands.length) * 100));
        }
        return resultList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<Bitmap> bitmaps) {
        super.onPostExecute(bitmaps);
        listDrawer.accept(bitmaps);
    }
}