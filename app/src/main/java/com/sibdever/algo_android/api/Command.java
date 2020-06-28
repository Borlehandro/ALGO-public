package com.sibdever.algo_android.api;

import java.util.HashMap;
import java.util.Map;

public enum Command {

    LOGIN("/user/login"),
    REGISTER("/user/register");

    private Map<String, String> arguments = new HashMap<>();
    private String path;

    Command(String path){
        this.path = path;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public String getPath() {
        return path;
    }
}
