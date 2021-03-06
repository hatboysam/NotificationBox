package com.habosa.notificationbox.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.habosa.notificationbox.BuildConfig;
import com.habosa.notificationbox.model.NotificationInfo;

/**
 * Database for the whole app.
 */
@Database(entities = {NotificationInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    @NonNull
    public static AppDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            RoomDatabase.Builder<AppDatabase> builder =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "app-database");

            if (BuildConfig.DEBUG) {
                builder = builder.fallbackToDestructiveMigration();
            }

            INSTANCE = builder.build();
        }

        return INSTANCE;
    }

    public abstract NotificationDao notificationDao();

}