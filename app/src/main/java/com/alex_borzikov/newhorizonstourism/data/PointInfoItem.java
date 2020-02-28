package com.alex_borzikov.newhorizonstourism.data;

public class PointInfoItem {

    private String name;
    private String descriptionName;
    private String pictureName;

    private Double locationX;
    private Double locationY;

    private int taskId;

    public PointInfoItem(String name, String descriptionName, String pictureName, String locationX, String locationY, String taskId) {

        this.name = name;
        this.descriptionName = descriptionName;
        this.pictureName = pictureName;
        this.locationX = Double.valueOf(locationX);
        this.locationY = Double.valueOf(locationY);
        this.taskId = Integer.parseInt(taskId);
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

    public Double getLocationX() {
        return locationX;
    }

    public Double getLocationY() {
        return locationY;
    }

    public int getTaskId() {
        return taskId;
    }
}