package com.alex_borzikov.newhorizonstourism;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
import com.alex_borzikov.newhorizonstourism.data.UserInfo;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<UserInfo> userInfo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showOpened = new MutableLiveData<>();
    private final MutableLiveData<Boolean> questStarted = new MutableLiveData<>();
    private final MutableLiveData<String> questId = new MutableLiveData<>();
    private final MutableLiveData<List<PointInfoItem>> pointsQueue = new MutableLiveData<>();

    public void setUserInfo(UserInfo item) {
        userInfo.setValue(item);
    }

    public LiveData<UserInfo> getUserInfo() {
        return userInfo;
    }

    public MutableLiveData<Boolean> getShowOpened() {
        return showOpened;
    }

    public void setShowOpened(boolean item) {
        showOpened.setValue(item);
    }

    public MutableLiveData<String> getQuestId() {
        return questId;
    }

    public void setQuestId(String id) {
        questId.setValue(id);
    }

    public MutableLiveData<Boolean> getQuestStarted() {
        return questStarted;
    }

    public void setQuestStarted(boolean item) {
        questStarted.setValue(item);
    }

    public MutableLiveData<List<PointInfoItem>> getPointsQueue() {
        return pointsQueue;
    }

    public void setPointsQueue(List<PointInfoItem> item) {
        pointsQueue.setValue(item);
    }

}
