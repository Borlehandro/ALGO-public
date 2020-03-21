package com.alex_borzikov.newhorizonstourism.api;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.alex_borzikov.newhorizonstourism.DrawableTask;

import java.io.IOException;

public class PictureTask extends AsyncTask<String, Void, Bitmap> {

    private DrawableTask drawer;

    public PictureTask(DrawableTask drawer){
        this.drawer = drawer;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        try {
            Bitmap bm = ApiClient.getImage(params[0]);
            Log.d("Borlehandro", String.valueOf(bm==null));
            Log.d("Borlehandro", "doInBackground: " + bm.getHeight());
            return ApiClient.getImage(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        drawer.onBitmapResult(bitmap);
    }
}
