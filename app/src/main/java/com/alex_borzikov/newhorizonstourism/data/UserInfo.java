package com.alex_borzikov.newhorizonstourism.data;

public class UserInfo {

    private int id;
    private String name;
    private String password;
    private String language;

    public UserInfo(int id, String name, String password, String language) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getLanguage() {
        return language;
    }
}
