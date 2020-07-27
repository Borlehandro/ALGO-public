package com.sibdever.algo_android.api.commands;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

abstract class Command {

    private static final String TAG = "Sibdever";

    // Release
    private static final String SERVER_URL = "http://Algo-env.eba-iaghw6qa.eu-west-2.elasticbeanstalk.com";

    // Debug
    // private static final String SERVER_URL = "http://algo-data.herokuapp.com/";

    protected Map<String, String> arguments;
    protected String path;

    protected Command(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public abstract static class CommandBuilder<T extends  CommandBuilder<T>> {

        protected Map<String, String> arguments = new HashMap<>();

        public T param(String key, String value) {
            arguments.put(key, value);
            return getThis();
        }

        public abstract Command build();

        protected abstract T getThis();

    }

    protected HttpURLConnection send(String connectionMethod) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL + path).openConnection();
        connection.setRequestMethod(connectionMethod);
        if(connectionMethod.equals("POST"))
            connection.setDoOutput(true);

        System.err.println("send: " + connection.getURL());

        OutputStream data = connection.getOutputStream();

        // BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(data, StandardCharsets.UTF_8));

        for(Map.Entry<String, String> item : this.arguments.entrySet()) {
            writer.write(item.getKey() + "=" + item.getValue());
            System.err.print(item.getKey() + "=" + item.getValue());
            writer.write("&");
            System.err.print("&");
        }
        System.err.println();

        writer.flush();
        writer.close();

        return connection;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }
}