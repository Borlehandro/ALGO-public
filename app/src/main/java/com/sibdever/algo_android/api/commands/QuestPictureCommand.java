package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class QuestPictureCommand extends PictureCommand {
    {
        path = "/quest/picture/";
    }

    public QuestPictureCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
