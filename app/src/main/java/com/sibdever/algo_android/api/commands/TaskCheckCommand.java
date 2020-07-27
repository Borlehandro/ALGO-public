package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class TaskCheckCommand extends InfoCommand {
    {
        path = "/task/check/";
    }

    private TaskCheckCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder>{

        @Override
        public TaskCheckCommand build() {
            return new TaskCheckCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
