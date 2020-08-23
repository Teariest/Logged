package com.clementf.logged;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.clementf.logged.activity_backend.ActivityEntity;
import com.clementf.logged.activity_backend.ActivityRepository;
import com.clementf.logged.timelog_backend.TimeLogEntity;
import com.clementf.logged.timelog_backend.TimeLogRepository;

import java.util.HashMap;
import java.util.List;

public class ActivitySelectorViewModel extends AndroidViewModel {

    public ActivitySelectorViewModel(@NonNull Application application) {
        super(application);

        activityRepository = ActivityRepository.getInstance(application);
        activities = activityRepository.getAllActivities();

        timeLogRepository = TimeLogRepository.getInstance(application);
        timeLogs = timeLogRepository.getAllTimeLogs();

        // update activityIDToIndex
        activities.observeForever(new Observer<List<ActivityEntity>>() {
            @Override
            public void onChanged(List<ActivityEntity> activityEntities) {
                activityIDToIndex = new HashMap<>();
                List<ActivityEntity> activityList = activityEntities;
                for (int i = 0; i < activityList.size(); i++) {
                    activityIDToIndex.put(activityList.get(i).getId(), i);
                }
            }
        });
    }

    // Activity related fields and methods

    private ActivityRepository activityRepository;
    private LiveData<List<ActivityEntity>> activities;


    public void insert(ActivityEntity activity) { activityRepository.insert(activity); }

    public void update(ActivityEntity activity) { activityRepository.update(activity); }

    public void delete(ActivityEntity activity) { activityRepository.delete(activity); }


    public LiveData<List<ActivityEntity>> getAllActivities() {
        return activities;
    }

    // Timelog related fields and methods

    private TimeLogRepository timeLogRepository;
    private LiveData<List<TimeLogEntity>> timeLogs;

    public void insert(TimeLogEntity timeLog) { timeLogRepository.insert(timeLog); }

    public void update(TimeLogEntity timeLog) { timeLogRepository.update(timeLog); }

    public void delete(TimeLogEntity timeLog) { timeLogRepository.delete(timeLog); }

    public LiveData<List<TimeLogEntity>> getAllTimeLogs() { return timeLogs; }

    // activityIDToIndexRelatedMethods

    private HashMap<Integer, Integer> activityIDToIndex;

    public int getIndexFromActivityID(int ID) {
        try {
            return activityIDToIndex.get(ID);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("ActivitySelectorViewModel.getIndexFromActivityID(int ID): Invalid ID parameter.");
        }
    }

    // ItemSelectorFragment related stuff

    private MutableLiveData<Integer> iconResource = new MutableLiveData<>();
    private MutableLiveData<Integer> colorResource = new MutableLiveData<>();

    public void setIconResource(int resource) {
        this.iconResource.setValue(resource);
    }

    public void setColorResource(int resource) {
        this.colorResource.setValue(resource);
    }

    public LiveData<Integer> getIconResource() { return iconResource; }

    public LiveData<Integer> getColorResource() { return colorResource; }
}
