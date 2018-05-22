package com.habosa.notificationbox;

import android.os.Bundle;
import android.util.Log;

import com.habosa.notificationbox.util.PreferenceUtils;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {

    private static final String TAG = "IntroActivity";

    private PreferenceUtils mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
        mPrefs = new PreferenceUtils(this);

        // TODO: Give credit

        // Icons:
        // https://www.flaticon.com/packs/web-design-development-ui

        // intro images
        // <div>Icons made by <a href="https://www.flaticon.com/authors/dinosoftlabs" title="DinosoftLabs">DinosoftLabs</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.intro_notifications)
                .title("Welcome to NotificationBox")
                .description("Manage your notifications like email.")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.intro_levels)
                .title("Take Control")
                .description("Fine tune which apps can notify you, and when.")
                .build());

        PermissionsSlideFragment permsFrag = PermissionsSlideFragment.getInstance(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.intro_start)
                .title("Let's Go!")
                .description("Grant access to your notifications to start using NotificationBox now."));
        addSlide(permsFrag, permsFrag.getBehavior());
    }


    @Override
    public void onFinish() {
        super.onFinish();

        Log.d(TAG, "onFinish");
        mPrefs.setHasShownIntro(true);
    }
}
