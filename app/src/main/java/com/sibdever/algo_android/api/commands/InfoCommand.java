package com.sibdever.algo_android.api.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public class InfoCommand extends Command {

    public static InfoBuilder builder(CommandType type) {
        return new InfoBuilder(type);
    }

    public static class InfoBuilder extends CommandBuilder<InfoBuilder> {

        protected InfoBuilder(CommandType type) {
            super(type);
        }

        @Override
        public InfoCommand build() {
            InfoCommand command = new InfoCommand(type);
            command.setArguments(arguments);
            return command;
        }

        @Override
        protected InfoBuilder getThis() {
            return this;
        }
    }

    protected InfoCommand(CommandType type) {
        super(type);
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