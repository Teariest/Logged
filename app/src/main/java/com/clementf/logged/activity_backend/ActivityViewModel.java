package com.clementf.logged.activity_backend;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.clementf.logged.activity_backend.ActivityEntity;
import com.clementf.logged.activity_backend.ActivityRepository;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewModel extends AndroidViewModel {

    public ActivityViewModel(@NonNull Application application) {
        super(application);

        activityRepository = ActivityRepository.getInstance(application);
        activities = activityRepository.getAllActivities();
    }

    private ActivityRepository activityRepository;
    private LiveData<List<ActivityEntity>> activities;


    public void insert(ActivityEntity activity) { activityRepository.insert(activity); }

    public void update(ActivityEntity activity) { activityRepository.update(activity); }

    public void delete(ActivityEntity activity) { activityRepository.delete(activity); }

    public LiveData<List<ActivityEntity>> getAllActivities() {
        return activities;
    }
}
