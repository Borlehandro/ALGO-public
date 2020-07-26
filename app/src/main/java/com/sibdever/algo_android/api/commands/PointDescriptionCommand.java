package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class PointDescriptionCommand extends DescriptionCommand {
    {
        path = "/point/description";
    }

    public PointDescriptionCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
