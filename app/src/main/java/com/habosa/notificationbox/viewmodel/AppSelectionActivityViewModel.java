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
import java.util.Collections;
import java.util.Comparator;
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

        BackgroundUtils.SERIAL.execute(new Runnable() {
            @Override
            public void run() {
                List<ApplicationInfo> infos = mPackageManager.getInstalledApplications(0);
                if (infos == null) {
                    mAppsLiveData.postValue(Resource.<List<AppDisplayInfo>>forFailure(new Exception("Load failed.")));
                    return;
                }

                List<AppDisplayInfo> displayInfos = new ArrayList<>();
                for (ApplicationInfo info : infos) {
                    String packageName = info.packageName;
                    String title = mPackageManager.getApplicationLabel(info).toString();
                    Drawable icon = mPackageManager.getApplicationIcon(info);

                    // Skip system apps that don't even have a name that's not their
                    // package name
                    if (title.equals(packageName)) {
                        continue;
                    }

                    displayInfos.add(new AppDisplayInfo(packageName, title, icon));
                }

                Collections.sort(displayInfos, new Comparator<AppDisplayInfo>() {
                    @Override
                    public int compare(AppDisplayInfo o1, AppDisplayInfo o2) {
                        return o1.name.compareTo(o2.name);
                    }
                });

                mAppsLiveData.postValue(Resource.forSuccess(displayInfos));
            }
        });
    }

    public LiveData<Resource<List<AppDisplayInfo>>> getApps() {
        return mAppsLiveData;
    }


}
