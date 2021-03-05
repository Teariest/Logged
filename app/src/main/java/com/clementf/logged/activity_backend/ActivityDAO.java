package com.clementf.logged.activity_backend;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.List;

@Dao
public interface ActivityDAO {

    @Insert
    void insert(ActivityEntity activityEntity);

    @Update
    void update(ActivityEntity activityEntity);

    @Delete
    void delete(ActivityEntity activityEntity);

    @Query("SELECT * FROM activity_table ORDER BY id ASC")
    LiveData<List<ActivityEntity>> getAllActivities();

    @Query("SELECT * FROM activity_table WHERE id = :ID")
    LiveData<ActivityEntity> getActivity(int ID);
}