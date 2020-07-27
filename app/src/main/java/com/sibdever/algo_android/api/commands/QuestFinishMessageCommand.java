package com.sibdever.algo_android.api.commands;

import com.sibdever.algo_android.data.QuestFinishMessage;

import java.util.Map;

public class QuestFinishMessageCommand extends InfoCommand {
    {
        path = "/quest/finish";
    }
    private QuestFinishMessageCommand(Map<String, String> arguments) {
        super(arguments);
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder> {

        @Override
        public QuestFinishMessageCommand build() {
            return new QuestFinishMessageCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
