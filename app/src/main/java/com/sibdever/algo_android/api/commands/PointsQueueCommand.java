package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class PointsQueueCommand extends InfoCommand {
    {
        path = "/point/queue/";
    }

    public PointsQueueCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
