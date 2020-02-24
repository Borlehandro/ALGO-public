package com.alex_borzikov.newhorizonstourism.data;

public class QuestListItem {

    private int id;
    private String name;
    private String descriptionShort;
    private int pointsNumber;

    public QuestListItem(String id, String name, String descriptionShort, String pointsNumber) {

        this.id = Integer.valueOf(id);
        this.name = name;
        this.descriptionShort = descriptionShort;
        this.pointsNumber = Integer.valueOf(pointsNumber);
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

}
