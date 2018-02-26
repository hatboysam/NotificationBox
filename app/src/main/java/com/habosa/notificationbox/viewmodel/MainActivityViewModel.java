package com.habosa.notificationbox.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.habosa.notificationbox.data.AppDatabase;
import com.habosa.notificationbox.data.NotificationDao;
import com.habosa.notificationbox.model.NotificationInfo;
import com.habosa.notificationbox.util.BackgroundUtils;

import java.util.List;

/**
 * Created by samstern on 1/23/18.
 */
public class MainActivityViewModel extends AndroidViewModel {

    private NotificationDao mNotificationDao;
    private MutableLiveData<List<NotificationInfo>> mNotificationInfos;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mNotificationDao = AppDatabase.getInstance(application).notificationDao();
        mNotificationInfos = new MutableLiveData<>();
    }

    public LiveData<List<NotificationInfo>> getNotificationInfo() {
        return mNotificationInfos;
    }

    public void requestNotificationInfos() {
        // TODO: Move to repository.
        BackgroundUtils.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                mNotificationInfos.postValue(mNotificationDao.getAll());
            }
        });
    }

}
