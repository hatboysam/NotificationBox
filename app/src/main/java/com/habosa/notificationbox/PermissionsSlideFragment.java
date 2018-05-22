package com.habosa.notificationbox;

import android.view.View;

import com.habosa.notificationbox.util.NotificationAccessUtils;

import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class PermissionsSlideFragment extends SlideFragment {

    public static PermissionsSlideFragment getInstance(SlideFragmentBuilder builder) {
        SlideFragment fragment = builder.build();

        PermissionsSlideFragment permissionsSlideFragment = new PermissionsSlideFragment();
        permissionsSlideFragment.setArguments(fragment.getArguments());

        return permissionsSlideFragment;
    }

    @Override
    public boolean canMoveFurther() {
        return NotificationAccessUtils.hasNotificationPermissions(getContext());
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "Grant notification access to continue.";
    }

    public MessageButtonBehaviour getBehavior() {
        return new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationAccessUtils.launchNotificationAccessSettings(getContext());
            }
        }, "Grant");
    }
}
