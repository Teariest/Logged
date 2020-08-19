package com.clementf.logged;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ActivityViewModel extends AndroidViewModel {

    private ActivityRepository activityRepository;
    private LiveData<List<ActivityEntity>> activities;


    public ActivityViewModel(@NonNull Application application) {
        super(application);
        activityRepository = new ActivityRepository(application);
        activities = activityRepository.getAllActivities();
    }

    public void insert(ActivityEntity activity) {
        activityRepository.insert(activity);
    }

    public void update(ActivityEntity activity) {
        activityRepository.update(activity);
    }

    public void delete(ActivityEntity activity) {
        activityRepository.delete(activity);
    }

    public LiveData<List<ActivityEntity>> getAllActivities() {
        return activityRepository.getAllActivities();
    }
}
