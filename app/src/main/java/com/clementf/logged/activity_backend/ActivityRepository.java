package com.clementf.logged.activity_backend;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.security.InvalidParameterException;
import java.util.List;

public class ActivityRepository {

    private static ActivityRepository instance;

    private ActivityDAO activityDao;
    private LiveData<List<ActivityEntity>> activities;

    private ActivityRepository(Application application) {
        ActivityDatabase database = ActivityDatabase.getInstance(application);
        activityDao = database.activityDao();
        activities = activityDao.getAllActivities();
    }

    public static ActivityRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ActivityRepository(application);
        }
        return instance;
    }

    public void insert(ActivityEntity activity) {
        new ActivityDBAsyncQuery(activityDao, ActivityDBAsyncQuery.INSERT_QUERY).execute(activity);
    }

    public void update(ActivityEntity activity) {
        new ActivityDBAsyncQuery(activityDao, ActivityDBAsyncQuery.UPDATE_QUERY).execute(activity);
    }

    public void delete(ActivityEntity activity) {
        new ActivityDBAsyncQuery(activityDao, ActivityDBAsyncQuery.DELETE_QUERY).execute(activity);
    }

    public LiveData<List<ActivityEntity>> getAllActivities() {
        return activities;
    }

    public LiveData<ActivityEntity> getActivity(int ID) {
        return activityDao.getActivity(ID);
    }

    // TODO: as this is deprecated, we should move to the current meta although not that important bc this is for testing
    public static class ActivityDBAsyncQuery extends AsyncTask<ActivityEntity, Void, Void> {

        public static final int INSERT_QUERY = 1;
        public static final int UPDATE_QUERY = 0;
        public static final int DELETE_QUERY = -1;

        private ActivityDAO dao;
        private int queryType;

        private ActivityDBAsyncQuery(ActivityDAO dao, int queryType) {

            if (queryType != INSERT_QUERY && queryType != UPDATE_QUERY && queryType != DELETE_QUERY) {
                throw new IllegalArgumentException("ActivityDBAsyncQuery provided with incorrect queryType");
            }
            this.dao = dao;
            this.queryType = queryType;
        }

        @Override
        protected Void doInBackground(ActivityEntity... activityEntities) {
            switch (queryType) {
                case INSERT_QUERY: dao.insert(activityEntities[0]);
                    break;
                case UPDATE_QUERY: dao.update(activityEntities[0]);
                    break;
                case DELETE_QUERY: dao.delete(activityEntities[0]);
                    break;
            }
            return null;
        }
    }
}
