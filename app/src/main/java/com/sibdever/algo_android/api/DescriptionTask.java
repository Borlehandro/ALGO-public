package com.sibdever.algo_android.api;

import android.os.AsyncTask;

import com.sibdever.algo_android.DescriptibleTask;

import java.io.IOException;

public class DescriptionTask extends AsyncTask<String, Void, StringBuffer> {

    private DescriptibleTask descriptor;

    public DescriptionTask(DescriptibleTask descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    protected StringBuffer doInBackground(String... params) {
        try {
            return ApiClient.getDescription(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(StringBuffer stringBuffer) {
        super.onPostExecute(stringBuffer);
        descriptor.onDescriptionResult(stringBuffer);
    }
}
