package com.clementf.logged.activity_editing;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.clementf.logged.activity_backend.ActivityEntity;
import com.clementf.logged.activity_backend.ActivityRepository;

import java.util.HashMap;
import java.util.List;

public class ActivityEditorViewModel extends AndroidViewModel {

    private static final String TAG = "ActivityEditorViewModel";

    public ActivityEditorViewModel(@NonNull Application application) {
        super(application);

        activityRepository = ActivityRepository.getInstance(application);
    }

    private ActivityRepository activityRepository;
    private LiveData<ActivityEntity> activity;

    public void insert(ActivityEntity activity) { activityRepository.insert(activity); }

    public void setCurrentActivity(int ID) {
        Log.d(TAG, "setCurrentActivity: activityID Requested: " + ID);
        activity = activityRepository.getActivity(ID);
    }

    public void setCurrentActivity(ActivityEntity activity) {
        MutableLiveData<ActivityEntity> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(activity);
        this.activity = mutableLiveData;
    }

    public boolean hasCurrentActivity() {
        return activity != null;
    }

    public LiveData<ActivityEntity> getActivity() {
        return activity;
    }

    public void updateAndPublishActivity(String name, String description) {

        Log.d(TAG, "updateAndPublishActivity: id: " + activity.getValue().getId());

        activityRepository.update(new ActivityEntity(
                activity.getValue().getId(),
                name,
                description,
                colorResource.getValue(),
                iconResource.getValue()
        ));
        activity = null;
        iconResource = new MutableLiveData<>();
        colorResource = new MutableLiveData<>();
    }

    public void createAndPublishActivity(String name, String description) {
        activityRepository.insert(new ActivityEntity(
                name,
                description,
                colorResource.getValue(),
                iconResource.getValue()
        ));
        activity = null;
        iconResource = new MutableLiveData<>();
        colorResource = new MutableLiveData<>();
    }

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
