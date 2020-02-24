package com.alex_borzikov.newhorizonstourism.data;

public class TaskInfoItem {

    private String descriptionShort;
    private String pictureName;

    private String choice1;
    private String choice2;
    private String choice3;

    public TaskInfoItem(String descriptionShort, String pictureName, String choice1, String choice2, String choice3) {

        this.descriptionShort = descriptionShort;
        this.pictureName = pictureName;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public String getPictureName() {
        return pictureName;
    }

    public String getChoice1() {
        return choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getChoice3() {
        return choice3;
    }
}
