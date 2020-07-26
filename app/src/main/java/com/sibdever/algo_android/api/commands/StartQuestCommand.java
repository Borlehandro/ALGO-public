package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class StartQuestCommand extends InfoCommand {
    {
        path = "/progress/start/";
    }

    public StartQuestCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
