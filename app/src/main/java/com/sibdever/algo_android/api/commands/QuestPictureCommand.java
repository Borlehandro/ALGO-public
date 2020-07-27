package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class QuestPictureCommand extends PictureCommand {
    {
        path = "/quest/picture/";
    }

    private QuestPictureCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends PictureBuilder<Builder>{

        @Override
        public QuestPictureCommand build() {
            return new QuestPictureCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
