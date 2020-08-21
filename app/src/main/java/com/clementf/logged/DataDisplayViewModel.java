package com.clementf.logged;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.clementf.logged.timelog_backend.TimeLogEntity;
import com.clementf.logged.timelog_backend.TimeLogRepository;

import java.util.List;

public class DataDisplayViewModel extends AndroidViewModel {

    public DataDisplayViewModel(@NonNull Application application) {
        super(application);

        timeLogRepository = TimeLogRepository.getInstance(application);
        timeLogs = timeLogRepository.getAllTimeLogs();
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

    public void delete(TimeLogEntity timeLog) {
        timeLogRepository.delete(timeLog);
    }

    public LiveData<List<TimeLogEntity>> getAllTimeLogs() {
        return timeLogRepository.getAllTimeLogs();
    }
}
