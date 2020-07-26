package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class NextPointCommand extends InfoCommand {
    {
        path = "/progress/next/";
    }

    public NextPointCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
