package com.sibdever.algo_android.api.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public class DescriptionCommand extends Command {

    public static DescriptionBuilder builder(CommandType type) {
        return new DescriptionBuilder(type);
    }

    public static class DescriptionBuilder extends CommandBuilder<DescriptionBuilder> {

        protected DescriptionBuilder(CommandType type) {
            super(type);
        }

        @Override
        public DescriptionCommand build() {
            DescriptionCommand command = new DescriptionCommand(type);
            command.setArguments(arguments);
            return command;
        }

        @Override
        protected DescriptionBuilder getThis() {
            return this;
        }
    }

    protected DescriptionCommand(CommandType type) {
        super(type);
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
