package com.sibdever.algo_android.data;

public class QuestListItem {

    private int id;
    private String name;
    private String descriptionShort;
    private int pointsNumber;
    private boolean completed;

    public QuestListItem(String id, String name, String descriptionShort, String pointsNumber, boolean completed) {

        this.id = Integer.parseInt(id);
        this.name = name;
        this.descriptionShort = descriptionShort;
        this.pointsNumber = Integer.parseInt(pointsNumber);
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public int getPointsNumber() {
        return pointsNumber;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
