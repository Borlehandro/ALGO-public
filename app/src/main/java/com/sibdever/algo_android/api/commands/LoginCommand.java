package com.sibdever.algo_android.api.commands;

import java.util.HashMap;
import java.util.Map;

public class LoginCommand extends InfoCommand {
    {
        path = "/user/login/";
    }

    private LoginCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder> {

        @Override
        public LoginCommand build() {
            return new LoginCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}