package com.sibdever.algo_android.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {

    private long id;
    private String description;
    private String choice1;
    private String choice2;
    private String choice3;

    private Task(long id, String description, String choice1, String choice2, String choice3) {
        this.id = id;
        this.description = description;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
    }

    public static Task valueOf(String json, String language) throws JSONException {

        JSONObject object = new JSONObject(json);

        switch (language) {
            case "en":
                language = "En";
                break;

            case "ru":
                language = "Ru";
                break;

            case "zh":
                language = "Zh";
                break;
        }
        return new Task(object.getLong("taskId"),
                object.getString("taskDesc" + language),
                object.getString("choice1" + language),
                object.getString("choice2" + language),
                object.getString("choice3" + language));

    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getChoice1() {
        return choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getChoice3() {
        return choice3;
    }
}