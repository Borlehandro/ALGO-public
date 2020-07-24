package com.sibdever.algo_android.data;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {
    private String name;
    private int bonuses;
    private int questsPassed;
    private int pointsPassed;
    private double kilometersCompleted;

    private UserInfo(String name, int bonuses, int questsPassed, int pointsPassed, double kilometersCompleted) {
        this.name = name;
        this.bonuses = bonuses;
        this.questsPassed = questsPassed;
        this.pointsPassed = pointsPassed;
        this.kilometersCompleted = kilometersCompleted;
    }

    public static UserInfo valueOf(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        return new UserInfo(object.getString("name"),
                object.getInt("bonuses"),
                object.getInt("questsPassed"),
                object.getInt("pointsPassed"),
                object.getDouble("kilometersCompleted"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBonuses() {
        return bonuses;
    }

    public void setBonuses(int bonuses) {
        this.bonuses = bonuses;
    }

    public int getQuestsPassed() {
        return questsPassed;
    }

    public void setQuestsPassed(int questsPassed) {
        this.questsPassed = questsPassed;
    }

    public int getPointsPassed() {
        return pointsPassed;
    }

    public void setPointsPassed(int pointsPassed) {
        this.pointsPassed = pointsPassed;
    }

    public double getKilometersCompleted() {
        return kilometersCompleted;
    }

    public void setKilometersCompleted(int kilometersCompleted) {
        this.kilometersCompleted = kilometersCompleted;
    }

}