package com.sibdever.algo_android.api;

import com.sibdever.algo_android.data.Point;
import com.sibdever.algo_android.data.TaskInfoItem;
import com.sibdever.algo_android.data.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class JsonParser {

    public static TaskInfoItem parseTaskInfo(String json) throws JSONException {

        JSONObject info = new JSONObject(json);

        return new TaskInfoItem(info.getString("taskDescEn"), info.getString("pic"),
                info.getString("chose1En"), info.getString("chose2En"), info.getString("chose3En"));

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