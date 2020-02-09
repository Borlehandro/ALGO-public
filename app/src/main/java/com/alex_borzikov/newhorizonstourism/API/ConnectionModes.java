package com.alex_borzikov.newhorizonstourism.API;

public enum ConnectionModes {

    REGISTER(1),
    LOGIN(2),
    GET_QUESTS_LIST(3),
    GET_QUEST_INFO(4),
    GET_POINT_INFO(5),
    CHECK_POINT_CODE(6),
    GET_TASK_INFO(7),
    CHECK_ANSWER(8);

    private final int value;

    ConnectionModes(int a){
        this.value = a;
    }

    public int getValue() {
        return value;
    }
}
