package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class LoginCommand extends InfoCommand {
    {
        path = "/user/login/";
    }

    public LoginCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
