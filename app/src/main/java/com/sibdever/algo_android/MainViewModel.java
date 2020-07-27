package com.sibdever.algo_android;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sibdever.algo_android.data.Quest;
import com.sibdever.algo_android.data.ShortPoint;

import java.util.LinkedList;

public class MainViewModel extends ViewModel {

    public MutableLiveData<BottomStates> getBottomSheetState() {
        return bottomSheetState;
    }

    public void setBottomSheetState(BottomStates state) {
        bottomSheetState.setValue(state);
    }

    public MutableLiveData<ShortPoint> getNextPoint() {
        return nextPoint;
    }

    public void setNextPoint(ShortPoint shortPoint) {
        this.nextPoint.setValue(shortPoint);
    }

    public enum BottomStates {
        QUEST_LIST_STATE,
        QUEST_DESCRIPTION_STATE,
        POINTS_QUEUE_START,
        POINTS_QUEUE_IN_PROCESS,
        POINTS_QUEUE_COMPLETED
    }

    private final MutableLiveData<BottomStates> bottomSheetState = new MutableLiveData<>();

    private final MutableLiveData<Boolean> showOpened = new MutableLiveData<>();
    private final MutableLiveData<Boolean> questStarted = new MutableLiveData<>();
    private final MutableLiveData<Boolean> questFinished = new MutableLiveData<>();
    private final MutableLiveData<Boolean> queueOpened = new MutableLiveData<>();
    private final MutableLiveData<String> questId = new MutableLiveData<>();
    private final MutableLiveData<LinkedList<ShortPoint>> pointsQueue = new MutableLiveData<>();
    private final MutableLiveData<ShortPoint> nextPoint = new MutableLiveData<>();

    // Test
    private final MutableLiveData<Quest> quest = new MutableLiveData<>();

    public MutableLiveData<Quest> getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest.setValue(quest);
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

    public MutableLiveData<LinkedList<ShortPoint>> getPointsQueue() {
        return pointsQueue;
    }

    public void setPointsQueue(LinkedList<ShortPoint> item) {
        pointsQueue.setValue(item);
    }

    public MutableLiveData<Boolean> getQuestFinished() {
        return questFinished;
    }

    public void setQuestFinished(boolean item) {
        questFinished.setValue(item);
    }

    public MutableLiveData<Boolean> getQueueOpened() {
        return queueOpened;
    }

    public void setQueueOpened(boolean item) {
        queueOpened.setValue(item);
    }

}
