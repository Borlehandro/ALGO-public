package com.sibdever.algo_android.api.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public abstract class DescriptionCommand extends Command {

    public DescriptionCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public StringBuffer execute(){
        try {

            HttpURLConnection connection = send("POST");

            BufferedReader content = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer buffer = new StringBuffer();

            content.lines().forEach(buffer::append);

            // Log.d(TAG, "Description must return: " + buffer.toString());

            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
