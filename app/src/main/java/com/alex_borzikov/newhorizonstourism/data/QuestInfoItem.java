package com.alex_borzikov.newhorizonstourism.data;

import com.alex_borzikov.newhorizonstourism.MainActivity;

public class QuestInfoItem {

    private String name;
    private String descriptionShort;
    private String descriptionName;
    private String pictureName;

    private int pointsCount;

    public QuestInfoItem(String name, String descriptionShort, String descriptionName, String pictureName, int pointsCount) {

        this.name = name;
        this.descriptionName = descriptionName;
        this.descriptionShort = descriptionShort;
        this.pictureName = pictureName;
        this.pointsCount = pointsCount;

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
