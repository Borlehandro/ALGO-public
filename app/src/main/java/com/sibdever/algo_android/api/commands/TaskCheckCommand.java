package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class TaskCheckCommand extends InfoCommand {
    {
        path = "/task/check/";
    }

    public TaskCheckCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
