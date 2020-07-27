package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class PointByCodeCommand extends InfoCommand {
    {
        path = "/point/info/";
    }

    private PointByCodeCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder>{
        @Override
        public PointByCodeCommand build() {
            return new PointByCodeCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
