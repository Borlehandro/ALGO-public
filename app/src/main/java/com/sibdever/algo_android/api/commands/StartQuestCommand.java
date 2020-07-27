package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class StartQuestCommand extends InfoCommand {
    {
        path = "/progress/start/";
    }

    private StartQuestCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder>{

        @Override
        public StartQuestCommand build() {
            return new StartQuestCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
