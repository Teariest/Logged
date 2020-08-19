package com.clementf.logged;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.nio.InvalidMarkException;
import java.security.InvalidParameterException;
import java.util.List;

public class ActivityRepository {

    private ActivityDAO activityDao;
    private LiveData<List<ActivityEntity>> activities;

    public ActivityRepository(Application application) {
        ActivityDatabase database = ActivityDatabase.getInstance(application);
        activityDao = database.activityDao();
        activities = activityDao.getAllActivities();
    }

    public void insert(ActivityEntity activity) {
        new DAOAsyncTask(activityDao, DAOAsyncTask.INSERT).doInBackground(activity);
    }

    public void update(ActivityEntity activity) {
        new DAOAsyncTask(activityDao, DAOAsyncTask.UPDATE).doInBackground(activity);
    }

    public void delete(ActivityEntity activity) {
        new DAOAsyncTask(activityDao, DAOAsyncTask.DELETE).doInBackground(activity);
    }

    public LiveData<List<ActivityEntity>> getAllActivities() {
        return activities;
    }

    // Async task to make database access calls (insert, update, delete) asynchronously
    private static class DAOAsyncTask extends AsyncTask<ActivityEntity, Void, Void> {

        public static final int INSERT = 1;
        public static final int UPDATE = 0;
        public static final int DELETE = -1;

        private ActivityDAO activityDAO;
        private int action;

        private DAOAsyncTask(ActivityDAO activityDAO, int action) {

            this.activityDAO = activityDAO;
            this.action = action;
        }

        @Override
        protected Void doInBackground(ActivityEntity... activityEntities) {

            switch (action) {
                case INSERT: activityDAO.insert(activityEntities[0]);
                    break;
                case UPDATE: activityDAO.update(activityEntities[0]);
                    break;
                case DELETE: activityDAO.delete(activityEntities[0]);
                    break;
                default: throw new InvalidParameterException("Action passed to InsertActivityAsyncTask is invalid.");
            }
            return null;
        }
    }
}
