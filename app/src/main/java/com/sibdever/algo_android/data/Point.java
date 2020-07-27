package com.sibdever.algo_android.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Point implements Serializable {

    private String name;
    private String descriptionName;
    private String pictureName;

    private Double latitude;
    private Double longitude;

    private long taskId;
    private long id;

    public Point(String name, String descriptionName, String pictureName,
                 Double latitude, Double longitude, long taskId, long id) {

        this.name = name;
        this.descriptionName = descriptionName;
        this.pictureName = pictureName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taskId = taskId;
        this.id = id;
    }

    public static Point valueOf(String json, String language) throws JSONException {

        JSONObject point = new JSONObject(json);

        String name = null;

        switch (language) {
            case "en":
                name = point.getString("nameEn");
                break;

            case "ru":
                name = point.getString("nameRu");
                break;

            case "zh":
                name = point.getString("nameZh");
                break;
        }

        return new Point(
                name,
                point.getString("descName"),
                point.getString("picName"),
                point.getDouble("latitude"),
                point.getDouble("longitude"),
                point.getLong("taskId"),
                point.getLong("pointId"));
    }

    public String getName() {
        return name;
    }

    public String getDescriptionName() {
        return descriptionName;
    }

    public String getPictureName() {
        return pictureName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public long getTaskId() {
        return taskId;
    }

    public long getId() {
        return id;
    }
}
