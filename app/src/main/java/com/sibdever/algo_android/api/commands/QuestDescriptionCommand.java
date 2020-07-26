package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class QuestDescriptionCommand extends DescriptionCommand {
    {
        path = "/quest/description/";
    }

    public QuestDescriptionCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
