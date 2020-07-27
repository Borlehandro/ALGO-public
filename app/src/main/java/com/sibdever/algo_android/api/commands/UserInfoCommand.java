package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class UserInfoCommand extends InfoCommand {
    {
        path = "/user/info/";
    }

    private UserInfoCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder>{

        @Override
        public UserInfoCommand build() {
            return new UserInfoCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
