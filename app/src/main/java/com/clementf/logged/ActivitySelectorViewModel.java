package com.clementf.logged;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.clementf.logged.activity_backend.ActivityEntity;
import com.clementf.logged.activity_backend.ActivityRepository;
import com.clementf.logged.timelog_backend.TimeLogEntity;
import com.clementf.logged.timelog_backend.TimeLogRepository;

import java.util.List;

public class ActivitySelectorViewModel extends AndroidViewModel {

    public ActivitySelectorViewModel(@NonNull Application application) {
        super(application);

        activityRepository = ActivityRepository.getInstance(application);
        activities = activityRepository.getAllActivities();

        timeLogRepository = TimeLogRepository.getInstance(application);
        timeLogs = timeLogRepository.getAllTimeLogs();
    }

    // Activity related fields and methods

    private ActivityRepository activityRepository;
    private LiveData<List<ActivityEntity>> activities;

    /* Not part of ActivitySelectorFragment requirements
    public void insert(ActivityEntity activity) {  activityRepository.insert(activity);
    }

    public void update(ActivityEntity activity) {
        activityRepository.update(activity);
    }

    public void delete(ActivityEntity activity) {
        activityRepository.delete(activity);
    }
    */

    public LiveData<List<ActivityEntity>> getAllActivities() {
        return activityRepository.getAllActivities();
    }

    // Timelog related fields and methods

    private TimeLogRepository timeLogRepository;
    private LiveData<List<TimeLogEntity>> timeLogs;

    public void insert(TimeLogEntity timeLog) {
        timeLogRepository.insert(timeLog);
    }

    public void update(TimeLogEntity timeLog) {
        timeLogRepository.update(timeLog);
    }

    /* Not part of ActivitySelectorFragment requirements
    public void delete(TimeLogEntity timeLog) {
        timeLogRepository.delete(timeLog);
    }

    public LiveData<List<TimeLogEntity>> getAllTimeLogs() {
        return timeLogRepository.getAllTimeLogs();
    }
    */

}
