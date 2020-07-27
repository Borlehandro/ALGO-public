package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class PointDescriptionCommand extends DescriptionCommand {
    {
        path = "/point/description";
    }

    private PointDescriptionCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DescriptionBuilder<Builder>{

        @Override
        public PointDescriptionCommand build() {
            return new PointDescriptionCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
