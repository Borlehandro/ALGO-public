package com.alex_borzikov.newhorizonstourism.API;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ServerTask extends AsyncTask<Map<String, String>, Void, String> {

    private static final String TAG = "Borlehandro";

    @SafeVarargs
    @Override
    protected final String doInBackground(Map<String, String>... params) {
        try {
            switch (Objects.requireNonNull(params[0].get("mode"))){
                case "GET_QUESTS_LIST":
                    Log.d(TAG, "It's quest");
                    return ApiClient.getGuestList();

                case "REGISTER":
                    Log.d(TAG, "It's registration");
                    return ApiClient.register(params[0].get("username"), params[0].get("password"), params[0].get("language"));

                case "LOGIN":
                    Log.d(TAG, "It's login");
                    return ApiClient.login(params[0].get("username"), params[0].get("password"));

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
