package com.alex_borzikov.newhorizonstourism.api;

import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
import com.alex_borzikov.newhorizonstourism.data.QuestInfoItem;
import com.alex_borzikov.newhorizonstourism.data.QuestListItem;
import com.alex_borzikov.newhorizonstourism.data.TaskInfoItem;

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
            res.add(new QuestListItem(object.getString("id"), object.getString("name"),
                    object.getString("short_description"), object.getString("points_count")));
        }

        return res;
    }

    public static QuestInfoItem parseQuestInfo(String json) throws JSONException {

        JSONObject info = new JSONObject(json);

        return new QuestInfoItem(info.getString("name"), info.getString("short_description"),
                info.getString("big_description"), info.getString("pic"),
                info.getString("points_count"));


    }

    public static PointInfoItem parsePointInfo(String json) throws JSONException {

        JSONObject info = new JSONObject(json);

        return new PointInfoItem(info.getString("name"),info.getString("big_description"),
                info.getString("pic"), info.getString("geolocation_x"),
                info.getString("geolocation_y"), info.getString("point_task"),
                info.getString("id"));

    }

    public static TaskInfoItem parseTaskInfo(String json) throws JSONException {

        JSONObject info = new JSONObject(json);

        return new TaskInfoItem(info.getString("desc"), info.getString("pic"),
                info.getString("chose1"), info.getString("chose2"), info.getString("chose3"));

    }

    public static LinkedList<PointInfoItem> parsePointsQueue(String json) throws JSONException {

        LinkedList<PointInfoItem> res = new LinkedList<>();

        JSONArray array = new JSONArray(json);

        for (int i = 0; i < array.length(); ++i) {
            JSONObject object = array.getJSONObject(i);
            res.add(new PointInfoItem(object.getString("name"),object.getString("big_description"),
                    object.getString("pic"), object.getString("geolocation_x"),
                    object.getString("geolocation_y"), object.getString("point_task"),
                    object.getString("id")));
        }

        return res;
    }
}