package com.habosa.notificationbox.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;

import com.habosa.notificationbox.livedata.NotificationsLiveData;

import java.util.List;

/**
 * Created by samstern on 1/23/18.
 */
public class MainActivityViewModel extends AndroidViewModel {

    private NotificationsLiveData mNotifications;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mNotifications = new NotificationsLiveData(application);
    }

    public LiveData<List<StatusBarNotification>> getNotifications() {
        return mNotifications;
    }

    public void requestNotifications() {
        mNotifications.refresh();
    }

}
