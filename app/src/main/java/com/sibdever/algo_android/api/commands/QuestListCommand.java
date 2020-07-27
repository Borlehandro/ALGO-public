package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class QuestListCommand extends InfoCommand {
    {
        path = "/quest/list/";
    }

    private QuestListCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder>{

        @Override
        public QuestListCommand build() {
            return new QuestListCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
