package com.sibdever.algo_android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Quest {

    private long id;
    private String name;
    private String descriptionShort;
    private String descriptionName;
    private String pictureName;
    private int bonuses;
    private int pointsCount;
    private StatusType status;
    private ShortPoint lastPoint;

    private Quest(long id, String name, String descriptionShort, String descriptionName, String pictureName, int bonuses, int pointsCount, StatusType status, ShortPoint lastPoint) {
        this.id = id;
        this.name = name;
        this.descriptionName = descriptionName;
        this.descriptionShort = descriptionShort;
        this.pictureName = pictureName;
        this.bonuses = bonuses;
        this.pointsCount = pointsCount;
        this.status = status;
        this.lastPoint = lastPoint;
    }

    public StatusType getStatus() {
        return status;
    }

    public ShortPoint getLastPoint() {
        return lastPoint;
    }

    // Copy from algo-data.QuestStatus
    public enum StatusType {
        NOT_STARTED,
        NEW,
        IN_PROGRESS_NOT_FINISHED,
        IN_PROGRESS_AGAIN,
        FINISHED,
        FINISHED_FIRST_TIME,
        FINISHED_AGAIN,
        NOT_CHANGED,
        ERROR
    }

    private static Quest valueOf(JSONObject info, String language) throws JSONException {
        JSONObject quest = info.getJSONObject("quest");
        JSONObject status = info.getJSONObject("status");

        String shortDesc = null;

        switch (language) {
            case "en":
                shortDesc = quest.getString("shortDescEn");
                break;

            case "ru":
                shortDesc = quest.getString("shortDescRu");
                break;

            case "zh":
                shortDesc = quest.getString("shortDescZh");
                break;
        }

        return new Quest(
                quest.getInt("questId"),
                quest.getString(language + "Name"),
                shortDesc,
                quest.getString("bigDescName"),
                quest.getString("picName"),
                quest.getInt("bonuses"),
                quest.getInt("pointsCount"),
                StatusType.valueOf(status.getString("status")),
                ShortPoint.valueOf(status.getJSONObject("point"), language));
    }

    public static Quest valueOf(String json, String language) throws JSONException {

        JSONObject info = new JSONObject(json);
        return valueOf(info, language);
    }

    public static List<Quest> listOf(String json, String language) throws JSONException {

        List<Quest> res = new ArrayList<>();

        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); ++i) {
            res.add(Quest.valueOf(array.getJSONObject(i), language));
        }

        return res;
    }

    public String getName() {
        return name;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public String getDescriptionName() {
        return descriptionName;
    }


    public String getPictureName() {
        return pictureName;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public int getBonuses() {
        return bonuses;
    }

    public long getId() {
        return id;
    }
}
