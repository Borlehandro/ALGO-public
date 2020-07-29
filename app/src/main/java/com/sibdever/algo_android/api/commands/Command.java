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

public abstract class Command {

    private static final String TAG = "Sibdever";

    // Release
    private static final String SERVER_URL = "http://Algo-env.eba-iaghw6qa.eu-west-2.elasticbeanstalk.com";

    // Debug
    // private static final String SERVER_URL = "http://algo-data.herokuapp.com/";

    protected Map<String, String> arguments;
    protected final String path;
    protected final CommandType type;

    protected Command(CommandType type) {
        this.type = type;
        this.path = type.path;
    }

    public enum CommandType {

        LOGIN("/user/login/"),
        REGISTER("/user/register/"),
        USER_INFO("/user/info/"),
        GET_QUEST_LIST("/quest/list/"),
        GET_QUEST_DESCRIPTION("/quest/description/"),
        GET_QUEST_PICTURE("/quest/picture/"),
        GET_QUEST_FINISH_MESSAGE("/quest/finish/"),
        GET_POINTS_QUEUE("/point/queue/"),
        START_QUEST("/progress/start/"),
        GET_POINT_INFO("/point/info/"), // POINT BY CODE. FOR QR ONLY!
        GET_POINT_DESCRIPTION("/point/description/"),
        GET_POINT_PICTURE("/point/picture/"),
        NEXT_POINT("/progress/next/"), // JUST FULL POINT INFO FOR CURRENT. FOR GEOLOCATION ONLY!
        GET_TASK_INFO("/task/info/"),
        CHECK_TASK("/task/check/");

        String path;

        CommandType(String path){
            this.path = path;
        }

    }

    public abstract static class CommandBuilder<T extends  CommandBuilder<T>> {

        protected Map<String, String> arguments = new HashMap<>();
        protected final CommandType type;

        protected CommandBuilder(CommandType type) {
            this.type = type;
        }

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

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }
}