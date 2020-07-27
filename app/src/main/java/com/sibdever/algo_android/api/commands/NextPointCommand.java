package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class NextPointCommand extends InfoCommand {
    {
        path = "/progress/next/";
    }

    private NextPointCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder> {

        @Override
        public NextPointCommand build() {
            return new NextPointCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
