package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class PointByCodeCommand extends InfoCommand {
    {
        path = "/point/info/";
    }

    public PointByCodeCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
