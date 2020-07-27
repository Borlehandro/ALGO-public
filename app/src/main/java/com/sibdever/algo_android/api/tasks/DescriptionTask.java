package com.sibdever.algo_android.api.tasks;

import android.os.AsyncTask;

import com.sibdever.algo_android.api.commands.DescriptionCommand;

import java.util.function.Consumer;

public class DescriptionTask extends AsyncTask<DescriptionCommand, Void, StringBuffer> {

    private Consumer<StringBuffer> descriptor;

    public DescriptionTask(Consumer<StringBuffer> descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    protected StringBuffer doInBackground(DescriptionCommand... command) {
        return command[0].execute();
    }

    @Override
    protected void onPostExecute(StringBuffer stringBuffer) {
        super.onPostExecute(stringBuffer);
        descriptor.accept(stringBuffer);
    }
}
