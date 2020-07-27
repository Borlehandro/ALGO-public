package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class QuestDescriptionCommand extends DescriptionCommand {
    {
        path = "/quest/description/";
    }

    private QuestDescriptionCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends DescriptionBuilder<Builder>{

        @Override
        public QuestDescriptionCommand build() {
            return new QuestDescriptionCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
