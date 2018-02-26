package com.habosa.notificationbox.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.habosa.notificationbox.model.NotificationInfo;

import java.util.List;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM NotificationInfo ORDER BY postTime DESC")
    List<NotificationInfo> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NotificationInfo info);


}
