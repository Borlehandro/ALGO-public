package com.alex_borzikov.newhorizonstourism.api;

import android.os.AsyncTask;

import java.io.IOException;

public class DescriptionTask extends AsyncTask<String, Void, StringBuffer> {
    @Override
    protected StringBuffer doInBackground(String... params) {
        try {
            return ApiClient.getDescription(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
