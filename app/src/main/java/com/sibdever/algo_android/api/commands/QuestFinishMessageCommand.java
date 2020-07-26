package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class QuestFinishMessageCommand extends InfoCommand {
    {
        path = "/quest/finish";
    }
    public QuestFinishMessageCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
