package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class PointsQueueCommand extends InfoCommand {
    {
        path = "/point/queue/";
    }

    private PointsQueueCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder> {

        @Override
        public PointsQueueCommand build() {
            return new PointsQueueCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
