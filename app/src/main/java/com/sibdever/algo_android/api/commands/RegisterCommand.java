package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class RegisterCommand extends InfoCommand {
    {
        path = "/user/register/";
    }

    private RegisterCommand(Map<String, String> arguments) {
        super(arguments);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends InfoBuilder<Builder>{

        @Override
        public RegisterCommand build() {
            return new RegisterCommand(arguments);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }

}
