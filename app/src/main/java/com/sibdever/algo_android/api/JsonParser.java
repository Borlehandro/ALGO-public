package com.sibdever.algo_android.api;

import android.util.Log;

import com.sibdever.algo_android.data.PointInfoItem;
import com.sibdever.algo_android.data.QuestInfoItem;
import com.sibdever.algo_android.data.QuestListItem;
import com.sibdever.algo_android.data.TaskInfoItem;
import com.sibdever.algo_android.data.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JsonParser {

    public static List<QuestListItem> parseQuestList(String json) throws JSONException {

        List<QuestListItem> res = new ArrayList<>();

        JSONArray array = new JSONArray(json);

        for (int i = 0; i < array.length(); ++i) {
            JSONObject object = array.getJSONObject(i);
            JSONObject quest = object.getJSONObject("quest");
            JSONObject status = object.getJSONObject("status");
            // Todo chose language here
            // Todo fix status
            res.add(new QuestListItem(quest.getString("questId"), quest.getString("enName"),
                    quest.getString("shortDescEn"), quest.getString("pointsCount"),
                    status.getString("status").equals("FINISHED")));
            Log.d("Sibdever", "parseQuestList: " + status.getString("status"));
        }

        return res;
    }

    // Todo: Check where I need it and remove!
    public static QuestInfoItem parseQuestInfo(String json) throws JSONException {

        JSONObject info = new JSONObject(json);

        return new QuestInfoItem(info.getString("name"), info.getString("short_description"),
                info.getString("big_description"), info.getString("pic"),
                info.getString("points_count"));

    }


    public static PointInfoItem parsePointInfo(String json) throws JSONException {

        JSONObject info = new JSONObject(json);

        return new PointInfoItem(info.getString("nameEn"), info.getString("descName"),
                info.getString("picName"), info.getString("latitude"),
                info.getString("longitude"), info.getString("taskId"),
                info.getString("pointId"));

    }

    public static TaskInfoItem parseTaskInfo(String json) throws JSONException {

        JSONObject info = new JSONObject(json);

        return new TaskInfoItem(info.getString("taskDescEn"), info.getString("pic"),
                info.getString("chose1En"), info.getString("chose2En"), info.getString("chose3En"));

    }

    public static LinkedList<PointInfoItem> parsePointsQueue(String json) throws JSONException {

        LinkedList<PointInfoItem> res = new LinkedList<>();

        JSONArray array = new JSONArray(json);

        for (int i = 0; i < array.length(); ++i) {
            JSONObject object = array.getJSONObject(i);
            res.add(new PointInfoItem(object.getString("nameEn"),object.getString("big_description"),
                    object.getString("picName"), object.getString("latitude"),
                    object.getString("longitude"), object.getString("point_task"),
                    object.getString("id")));
        }

        return res;
    }

    public static UserInfo parseUserInfo(String json) throws JSONException {

        JSONObject info = new JSONObject(json);

        return new UserInfo(info.getString("user_name"),
                Integer.parseInt(info.getString("quests_completed")),
                Integer.parseInt(info.getString("bonuses")),
                Integer.parseInt(info.getString("points_completed")),
                Float.parseFloat(info.getString("kilometers_completed")));

    }

}