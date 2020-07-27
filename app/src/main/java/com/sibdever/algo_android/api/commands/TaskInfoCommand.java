package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class TaskInfoCommand extends InfoCommand {
    {
        path = "/task/info/";
    }

    private TaskInfoCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder>{

        @Override
        public TaskInfoCommand build() {
            return new TaskInfoCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
