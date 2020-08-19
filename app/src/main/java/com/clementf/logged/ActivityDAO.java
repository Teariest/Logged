package com.clementf.logged;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
    @Query("SELECT * FROM activity_table ORDER BY `order` DESC")
    LiveData<List<ActivityEntity>> getAllActivities();
}