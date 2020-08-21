package com.clementf.logged.timelog_backend;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.clementf.logged.activity_backend.ActivityDAO;
import com.clementf.logged.activity_backend.ActivityEntity;

import java.util.List;

@Dao
public interface TimeLogDAO {

    @Insert
    void insert(TimeLogEntity timeLogEntity);

    @Update
    void update(TimeLogEntity timeLogEntity);

    @Delete
    void delete(TimeLogEntity timeLogEntity);

    @Query("SELECT * FROM timelog_table ORDER BY timelog ASC")
    LiveData<List<TimeLogEntity>> getAllTimeLogs();
}
