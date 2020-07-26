package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class QuestListCommand extends InfoCommand {
    {
        path = "/quest/list/";
    }

    public QuestListCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
