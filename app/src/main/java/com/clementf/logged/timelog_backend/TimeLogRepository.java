package com.clementf.logged.timelog_backend;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TimeLogRepository {

    private static TimeLogRepository instance;

    private TimeLogDAO timeLogDAO;
    private LiveData<List<TimeLogEntity>> timeLogs;

    private TimeLogRepository(Application application) {
        TimeLogDatabase database = TimeLogDatabase.getInstance(application);
        timeLogDAO = database.timeLogDAO();
        timeLogs = timeLogDAO.getAllTimeLogs();
    }

    public static TimeLogRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TimeLogRepository(application);
        }
        return instance;
    }

    public void insert(TimeLogEntity timeLog) {
        new TimeLogDBAsyncQuery(timeLogDAO, TimeLogDBAsyncQuery.INSERT_QUERY).execute(timeLog);
    }

    public void update(TimeLogEntity timeLog) {
        new TimeLogDBAsyncQuery(timeLogDAO, TimeLogDBAsyncQuery.UPDATE_QUERY).execute(timeLog);
    }

    public void delete(TimeLogEntity timeLog) {
        new TimeLogDBAsyncQuery(timeLogDAO, TimeLogDBAsyncQuery.DELETE_QUERY).execute(timeLog);
    }

    public LiveData<List<TimeLogEntity>> getAllTimeLogs() { return timeLogs; }

    public LiveData<List<TimeLogEntity>> getTimeLogsSince(long earliestTime) {

        // I hope this works but it could take a long time, idk
        return timeLogDAO.getTimeLogsSince(earliestTime);
    }

    // No idea why I made this but I'm keeping it till getTimeLogsSince(long) works
    // TODO: as this is deprecated, we should move to the current meta although not that important bc this is for testing
    public static class TimeLogDBAsyncFetchQuery extends AsyncTask<Long, Void, LiveData<List<TimeLogEntity>>> {

        private TimeLogDAO dao;

        private TimeLogDBAsyncFetchQuery(TimeLogDAO dao) {

            this.dao = dao;
        }

        @Override
        protected LiveData<List<TimeLogEntity>> doInBackground(Long... longs) {

            if (longs[0] < 0) {
                throw new IllegalArgumentException("ActivityDBAsyncQuery provided with illegal earliestTime");
            }

            return dao.getTimeLogsSince(longs[0]);
        }

        @Override
        protected void onPostExecute(LiveData<List<TimeLogEntity>> listLiveData) {
            super.onPostExecute(listLiveData);
        }
    }

    // TODO: as this is deprecated, we should move to the current meta although not that important bc this is for testing
    public static class TimeLogDBAsyncQuery extends AsyncTask<TimeLogEntity, Void, Void> {

        public static final int INSERT_QUERY = 1;
        public static final int UPDATE_QUERY = 0;
        public static final int DELETE_QUERY = -1;

        private TimeLogDAO dao;
        private int queryType;

        private TimeLogDBAsyncQuery(TimeLogDAO dao, int queryType) {

            if (queryType != INSERT_QUERY && queryType != UPDATE_QUERY && queryType != DELETE_QUERY) {
                throw new IllegalArgumentException("ActivityDBAsyncQuery provided with incorrect queryType");
            }
            this.dao = dao;
            this.queryType = queryType;
        }

        @Override
        protected Void doInBackground(TimeLogEntity... timeLogEntities) {
            switch (queryType) {
                case INSERT_QUERY: dao.insert(timeLogEntities[0]);
                    break;
                case UPDATE_QUERY: dao.update(timeLogEntities[0]);
                    break;
                case DELETE_QUERY: dao.delete(timeLogEntities[0]);
                    break;
            }
            return null;
        }
    }
}
