package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class RegisterCommand extends InfoCommand {
    {
        path = "/user/register/";
    }

    public RegisterCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
