package com.sibdever.algo_android.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

// Copy from algo-data
public class QuestStatus implements Serializable {

    // Todo NOT_CHANGED? Really?
    public enum StatusType {
        NOT_STARTED,
        NEW,
        IN_PROGRESS_NOT_FINISHED,
        IN_PROGRESS_AGAIN,
        FINISHED,
        FINISHED_FIRST_TIME,
        FINISHED_AGAIN,
        NOT_CHANGED,
        ERROR
    }

    private StatusType status;
    private ShortPoint point;

    private QuestStatus(StatusType status, ShortPoint point) {
        this.status = status;
        this.point = point;
    }

    public static QuestStatus valueOf(String json, String language) throws JSONException {
        return QuestStatus.valueOf(new JSONObject(json), language);
    }

    public static QuestStatus valueOf(JSONObject json, String language) throws JSONException {
        return new QuestStatus(StatusType.valueOf(json.getString("status")), ShortPoint.valueOf(json.getJSONObject("point"), language));
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public ShortPoint getPoint() {
        return point;
    }

    public void setPoint(ShortPoint point) {
        this.point = point;
    }

}