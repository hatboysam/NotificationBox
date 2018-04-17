package com.habosa.notificationbox.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.habosa.notificationbox.data.AppDatabase;
import com.habosa.notificationbox.data.NotificationDao;
import com.habosa.notificationbox.model.NotificationDisplayInfo;
import com.habosa.notificationbox.model.NotificationInfo;
import com.habosa.notificationbox.util.BackgroundUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samstern on 1/23/18.
 */
public class MainActivityViewModel extends AndroidViewModel {

    private NotificationDao mNotificationDao;
    private MutableLiveData<List<NotificationDisplayInfo>> mNotificationInfos;

    private final PackageManager mPackageManager;
    private final Resources mResources;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mPackageManager = application.getPackageManager();
        mResources = application.getResources();

        mNotificationDao = AppDatabase.getInstance(application).notificationDao();
        mNotificationInfos = new MutableLiveData<>();
    }

    public LiveData<List<NotificationDisplayInfo>> getNotifications() {
        return mNotificationInfos;
    }

    public void dismissAll() {
        BackgroundUtils.SERIAL.execute(new Runnable() {
            @Override
            public void run() {
                mNotificationDao.deleteAll();
            }
        });

        requestNotificationInfos();
    }

    public void removeNotification(final NotificationInfo info) {
        // TODO: Move to repository
        BackgroundUtils.SERIAL.execute(new Runnable() {
            @Override
            public void run() {
                mNotificationDao.delete(info);
            }
        });

        requestNotificationInfos();
    }

    public void requestNotificationInfos() {
        // TODO: Move to repository.
        BackgroundUtils.SERIAL.execute(new Runnable() {
            @Override
            public void run() {
                List<NotificationInfo> infos = mNotificationDao.getAll();
                ArrayList<NotificationDisplayInfo> displayInfos = new ArrayList<>(infos.size());
                for (NotificationInfo ni : infos) {
                    NotificationDisplayInfo ndi = new NotificationDisplayInfo(ni);
                    ndi.load(mPackageManager, mResources);

                    displayInfos.add(ndi);
                }


                mNotificationInfos.postValue(displayInfos);
            }
        });
    }

}
