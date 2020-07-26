package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class UserInfoCommand extends InfoCommand {
    {
        path = "/user/info/";
    }

    public UserInfoCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
