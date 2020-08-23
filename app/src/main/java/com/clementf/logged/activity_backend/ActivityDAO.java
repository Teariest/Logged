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

    // TODO: 'order' might be wrong but I think the quotations are to signify order is a name not a command
    @Query("SELECT * FROM activity_table ORDER BY activity DESC")
    LiveData<List<ActivityEntity>> getAllActivities();

    @Query("SELECT *, :ID FROM activity_table")
    LiveData<ActivityEntity> getActivity(int ID);
}