package com.alex_borzikov.newhorizonstourism;

import android.os.AsyncTask;
import java.io.IOException;

public class ServerTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        try {
            return ApiClient.getInstance().connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
