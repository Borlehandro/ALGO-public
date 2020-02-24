package com.alex_borzikov.newhorizonstourism.api;

import com.alex_borzikov.newhorizonstourism.data.QuestListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static List<QuestListItem> parseQuestList(String json) throws JSONException {

        List<QuestListItem> res = new ArrayList<>();

        JSONArray array = new JSONArray(json);

        for (int i = 0; i < array.length(); ++i) {
            JSONObject object = array.getJSONObject(i);
            res.add(new QuestListItem(object.getString("id"), object.getString("name"), object.getString("short_description"), object.getString("points_count")));
        }

        return res;
    }

}