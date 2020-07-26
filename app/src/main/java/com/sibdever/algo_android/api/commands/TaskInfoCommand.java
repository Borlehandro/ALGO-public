package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class TaskInfoCommand extends InfoCommand {
    {
        path = "/task/info/";
    }

    public TaskInfoCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
