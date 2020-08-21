package com.clementf.logged;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.clementf.logged.activity_backend.ActivityEntity;
import com.clementf.logged.activity_backend.ActivityRepository;

import java.util.List;

public class ActivityEditorViewModel extends AndroidViewModel {

    public ActivityEditorViewModel(@NonNull Application application) {
        super(application);

        activityRepository = ActivityRepository.getInstance(application);
    }

    private ActivityRepository activityRepository;

    public void update(ActivityEntity activity) {
        activityRepository.update(activity);
    }

    public LiveData<ActivityEntity> getActivity(int ID) {
        return activityRepository.getActivity(ID);
    }
}
