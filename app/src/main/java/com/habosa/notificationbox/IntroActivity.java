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
