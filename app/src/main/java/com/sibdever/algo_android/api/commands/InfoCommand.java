package com.sibdever.algo_android.api.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public abstract class InfoCommand extends Command {

    protected InfoCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public abstract static class InfoBuilder<T extends InfoBuilder<T>> extends CommandBuilder<T> {
        // empty
    }

    public String execute() {
        try {

            HttpURLConnection connection = send("POST");

            InputStream content = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            StringBuilder builder;
            builder = reader.lines().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
            reader.close();

            connection.disconnect();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}