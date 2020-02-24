package com.alex_borzikov.newhorizonstourism.data;

public class QuestInfoItem {

    private String name;
    private String descriptionShort;
    private String descriptionName;
    private String pictureName;

    private int pointsCount;

    public QuestInfoItem(String name, String descriptionShort, String descriptionName, String pictureName, String pointsCount) {

        this.name = name;
        this.descriptionName = descriptionName;
        this.descriptionShort = descriptionShort;
        this.pictureName = pictureName;
        this.pointsCount = Integer.parseInt(pointsCount);

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
}
