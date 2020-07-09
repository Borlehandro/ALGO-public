package com.sibdever.algo_android.api;

import android.os.AsyncTask;
import android.util.Log;

import com.sibdever.algo_android.ResponsibleTask;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class InfoTask extends AsyncTask<Command, Void, String> {

    private ResponsibleTask responder;

    private static final String TAG = "Borlehandro";

    public InfoTask(ResponsibleTask responder) {
        this.responder = responder;
    }

    @Override
    protected final String doInBackground(Command... commands) {
        return ApiClient.send(commands[0]);
//        try {
//            switch (Objects.requireNonNull(params[0].get("mode"))) {
//
//                case "GET_QUESTS_LIST":
//                    System.out.println("It's quest list with lang " + params[0].get("language"));
//                    Log.d(TAG, "It's quest list with lang " + params[0].get("language"));
//                    return ApiClient.getGuestList(params[0].get("language"), params[0].get("userTicket"));
//
//                case "GET_QUEST_INFO":
//                    Log.d(TAG, "It's quest info");
//                    return ApiClient.getQuestInfo(params[0].get("questId"), params[0].get("language"));
//
//                case "GET_POINTS_QUEUE":
//                    Log.d(TAG, "It's points queue");
//                    return ApiClient.getPointsQueue(params[0].get("questId"), params[0].get("language"));
//
//                case "GET_POINT_INFO":
//                    Log.d(TAG, "It's point info");
//                    return ApiClient.getPointInfo(params[0].get("pointId"), params[0].get("language"));
//
//                case "GET_TASK_INFO":
//                    Log.d(TAG, "It's task info");
//                    return ApiClient.getTaskInfo(params[0].get("taskId"), params[0].get("language"),
//                            params[0].get("userTicket"));
//
//                case "REGISTER":
//                    Log.d(TAG, "It's registration");
//                    return ApiClient.register(params[0].get("username"), params[0].get("password"),
//                            params[0].get("language"));
//
//                case "LOGIN":
//                    Log.d(TAG, "It's login");
//                    return ApiClient.login(params[0].get("username"), params[0].get("password"));
//
//                case "CHECK_CODE":
//                    Log.d(TAG, "It's code checking");
//                    return ApiClient.checkPointCode(params[0].get("code"));
//
//                case "CHECK_ANSWER":
//                    Log.d(TAG, "It's answer checking");
//                    return ApiClient.checkTaskAnswer(params[0].get("answerIndex"),
//                            params[0].get("taskId"),
//                            params[0].get("userTicket"));
//
//                case "GET_USER_INFO":
//                    Log.d(TAG, "It's get user info");
//                    return ApiClient.getUserInfo(params[0].get("userTicket"));
//
//                case "SET_COMPLETED":
//                    Log.d(TAG, "It's set completed");
//                    return ApiClient.setCompleted(params[0].get("userTicket"), params[0].get("questId"));
//
//                case "CHECK_NOT_COMPLETED":
//                    Log.d(TAG, "It's check not completed");
//                    return ApiClient.checkNotCompleted(params[0].get("userTicket"), params[0].get("questId"));
//            }
//        } catch (IOException | NullPointerException e) {
//            Log.e(TAG, "doInBackground: " + e.getMessage());
//            // e.printStackTrace();
//        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        responder.onTaskResponse(s);
    }
}
