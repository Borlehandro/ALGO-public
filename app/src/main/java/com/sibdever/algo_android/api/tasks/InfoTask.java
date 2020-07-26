package com.sibdever.algo_android.api.tasks;

import android.os.AsyncTask;

import com.sibdever.algo_android.api.commands.InfoCommand;

import java.util.function.Consumer;

public class InfoTask extends AsyncTask<InfoCommand, Void, String> {

    private Consumer<String> responder;

    private static final String TAG = "Borlehandro";

    public InfoTask(Consumer<String> responder) {
        this.responder = responder;
    }

    @Override
    protected final String doInBackground(InfoCommand... commands) {
        return commands[0].execute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        responder.accept(s);
    }
}
