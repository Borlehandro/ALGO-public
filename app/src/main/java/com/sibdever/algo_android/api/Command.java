package com.sibdever.algo_android.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Command {

    LOGIN("/user/login/", ContentType.STANDARD),
    REGISTER("/user/register/", ContentType.STANDARD),
    USER_INFO("/user/info/", ContentType.STANDARD),
    GET_QUEST_LIST("/quest/list/", ContentType.STANDARD),
    GET_QUEST_DESCRIPTION("/quest/description/", ContentType.TEXT),
    GET_QUEST_PICTURE("/quest/picture/", ContentType.PICTURE),
    GET_POINTS_QUEUE("/point/queue/", ContentType.STANDARD),
    START_QUEST("/progress/start/", ContentType.STANDARD),
    GET_POINT_INFO("/point/info/", ContentType.STANDARD), /** POINT BY CODE. FOR QR ONLY!*/
    GET_POINT_DESCRIPTION("/point/description/", ContentType.TEXT),
    GET_POINT_PICTURE("/point/picture/", ContentType.PICTURE),
    NEXT_POINT("/progress/next/", ContentType.STANDARD), // JUST FULL POINT INFO FOR CURRENT. FOR GEOLOCATION ONLY!
    GET_TASK_INFO("/task/info/", ContentType.STANDARD),
    CHECK_TASK("/task/check/", ContentType.STANDARD);

    private Map<String, String> arguments = new HashMap<>();
    private String path;
    private ContentType contentType;

    public enum ContentType {
        STANDARD,
        PICTURE,
        TEXT
    }

    Command(String path, ContentType type){
        this.path = path;
        this.contentType = type;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public void setArguments(String[][] params) {
        Map<String, String> map = new HashMap<>();
        Arrays.asList(params).forEach(item -> map.put(item[0], item[1]));
        this.arguments = map;
    }

    public String getPath() {
        return path;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
