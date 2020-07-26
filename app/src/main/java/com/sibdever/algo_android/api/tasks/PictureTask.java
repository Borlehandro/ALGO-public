package com.sibdever.algo_android.api.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.sibdever.algo_android.api.commands.PictureCommand;

import java.util.function.Consumer;

public class PictureTask extends AsyncTask<PictureCommand, Void, Bitmap> {

    private Consumer<Bitmap> drawer;

    public PictureTask(Consumer<Bitmap> drawer) {
        this.drawer = drawer;
    }

    @Override
    protected Bitmap doInBackground(PictureCommand... commands) {
        return commands[0].execute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        drawer.accept(bitmap);
    }
}
