package com.sibdever.algo_android.data;

public class UserInfo {
    private String userName;
    private int questsCompleted;
    private int bonuses;
    private int pointsCompleted;
    private float kilometersCompleted;

    public UserInfo(String userName, int questsCompleted, int bonuses, int pointsCompleted, float kilometersCompleted) {
        this.userName = userName;
        this.questsCompleted = questsCompleted;
        this.bonuses = bonuses;
        this.pointsCompleted = pointsCompleted;
        this.kilometersCompleted = kilometersCompleted;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getQuestsCompleted() {
        return questsCompleted;
    }

    public void setQuestsCompleted(int questsCompleted) {
        this.questsCompleted = questsCompleted;
    }

    public int getBonuses() {
        return bonuses;
    }

    public void setBonuses(int bonuses) {
        this.bonuses = bonuses;
    }

    public int getPointsCompleted() {
        return pointsCompleted;
    }

    public void setPointsCompleted(int pointsCompleted) {
        this.pointsCompleted = pointsCompleted;
    }

    public float getKilometersCompleted() {
        return kilometersCompleted;
    }

    public void setKilometersCompleted(int kilometersCompleted) {
        this.kilometersCompleted = kilometersCompleted;
    }
}
