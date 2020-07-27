package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class PointPictureCommand extends PictureCommand {
    {
        path = "/point/picture/";
    }

    private PointPictureCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends PictureBuilder<Builder>{

        @Override
        public PointPictureCommand build() {
            return new PointPictureCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
