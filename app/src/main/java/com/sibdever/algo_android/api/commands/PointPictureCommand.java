package com.sibdever.algo_android.api.commands;

import java.util.Map;

public class PointPictureCommand extends PictureCommand {
    {
        path = "/point/picture/";
    }

    public PointPictureCommand(Map<String, String> arguments) {
        super(arguments);
    }
}
