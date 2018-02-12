package com.habosa.notificationbox.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.habosa.notificationbox.model.AppDisplayInfo;
import com.habosa.notificationbox.util.BackgroundUtils;
import com.habosa.notificationbox.util.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for {@link com.habosa.notificationbox.AppSelectionActivity}.
 */
public class AppSelectionActivityViewModel extends AndroidViewModel {

    private PackageManager mPackageManager;

    private MutableLiveData<Resource<List<AppDisplayInfo>>> mAppsLiveData = new MutableLiveData<>();

    public AppSelectionActivityViewModel(@NonNull Application application) {
        super(application);

        mPackageManager = application.getPackageManager();
    }

    public void startLoadApps() {
        mAppsLiveData.setValue(Resource.<List<AppDisplayInfo>>forLoading());

        BackgroundUtils.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                List<ApplicationInfo> infos = mPackageManager.getInstalledApplications(0);
                if (infos == null) {
                    mAppsLiveData.postValue(Resource.<List<AppDisplayInfo>>forFailure(new Exception("Load failed.")));
                    return;
                }

                List<AppDisplayInfo> displayInfos = new ArrayList<>();
                for (ApplicationInfo info : infos) {
                    String title = mPackageManager.getApplicationLabel(info).toString();
                    Drawable icon = mPackageManager.getApplicationIcon(info);

                    displayInfos.add(new AppDisplayInfo(title, icon));
                }

                mAppsLiveData.postValue(Resource.forSuccess(displayInfos));
            }
        });
    }

    public LiveData<Resource<List<AppDisplayInfo>>> getApps() {
        return mAppsLiveData;
    }


}
