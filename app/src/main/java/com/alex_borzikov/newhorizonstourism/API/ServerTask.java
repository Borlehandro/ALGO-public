package com.alex_borzikov.newhorizonstourism.API;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ServerTask extends AsyncTask<Map<String, ConnectionModes>, Void, String> {

    @SafeVarargs
    @Override
    protected final String doInBackground(Map<String, ConnectionModes>... params) {
        try {
            switch (Objects.requireNonNull(params[0].get("mode"))){
                case GET_QUESTS_LIST:
                    Log.d("Borlehandro", "It's quest");
                    return ApiClient.getGuestList();
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
