package com.sibdever.algo_android.data;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestFinishMessage {

        private final String finishMessage;
        private final int bonuses;

        private QuestFinishMessage(String finishMessage, int bonuses) {
            this.finishMessage = finishMessage;
            this.bonuses = bonuses;
        }

        public static QuestFinishMessage valueOf(String json) throws JSONException {
            JSONObject object = new JSONObject(json);
            return new QuestFinishMessage(object.getString("finishMessage"), object.getInt("bonuses"));
        }

        public String getFinishMessage() {
            return finishMessage;
        }

        public int getBonuses() {
            return bonuses;
        }

}
