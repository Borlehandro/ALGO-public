package com.sibdever.algo_android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;

public class ShortPoint implements Serializable {

    private String pictureName;
    private String name;

    private Double latitude;
    private Double longitude;

    private ShortPoint(String pictureName, String name, Double latitude, Double longitude) {
        this.pictureName = pictureName;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    static ShortPoint valueOf(JSONObject point, String language) throws JSONException {

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

        return new ShortPoint(point.getString("picName"), name, point.getDouble("latitude"), point.getDouble("longitude"));
    }

    public static ShortPoint valueOf(String json, String language) throws JSONException {
        JSONObject point = new JSONObject(json);
        return valueOf(point, language);
    }

    public static LinkedList<ShortPoint> listOf(String json, String language) throws JSONException {

        LinkedList<ShortPoint> res = new LinkedList<>();
        JSONArray array = new JSONArray(json);

        for(int i = 0; i < array.length(); i++) {
            res.add(ShortPoint.valueOf(array.getJSONObject(i), language));
        }

        return res;
    }

    public String getPictureName() {
        return pictureName;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
