package com.alex_borzikov.newhorizonstourism;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
import com.alex_borzikov.newhorizonstourism.data.UserInfo;

import java.util.LinkedList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<UserInfo> userInfo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showOpened = new MutableLiveData<>();
    private final MutableLiveData<Boolean> needPointsQueue = new MutableLiveData<>();
    private final MutableLiveData<Boolean> questStarted = new MutableLiveData<>();
    private final MutableLiveData<Boolean> questFinished = new MutableLiveData<>();
    private final MutableLiveData<Boolean> queueOpened = new MutableLiveData<>();
    private final MutableLiveData<String> questId = new MutableLiveData<>();
    private final MutableLiveData<LinkedList<PointInfoItem>> pointsQueue = new MutableLiveData<>();

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

    public MutableLiveData<LinkedList<PointInfoItem>> getPointsQueue() {
        return pointsQueue;
    }

    public void setPointsQueue(LinkedList<PointInfoItem> item) {
        pointsQueue.setValue(item);
    }

    public MutableLiveData<Boolean> getQuestFinished() {
        return questFinished;
    }

    public void setQuestFinished(boolean item) {
        questFinished.setValue(item);
    }

    public MutableLiveData<Boolean> getNeedPointsQueue() {
        return needPointsQueue;
    }

    public void setNeedPointsQueue(boolean item){needPointsQueue.setValue(item);}

    public MutableLiveData<Boolean> getQueueOpened() {
        return queueOpened;
    }

    public void setQueueOpened(boolean item) {
        queueOpened.setValue(item);
    }
}
