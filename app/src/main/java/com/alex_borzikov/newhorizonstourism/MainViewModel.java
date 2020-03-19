package com.alex_borzikov.newhorizonstourism;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alex_borzikov.newhorizonstourism.data.UserInfo;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<UserInfo> userInfo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showOpened = new MutableLiveData<>();

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

}
