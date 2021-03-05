package com.clementf.logged.activity_backend;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.clementf.logged.R;

@Database(entities = ActivityEntity.class, version = 1)
public abstract class ActivityDatabase extends RoomDatabase {

    private static ActivityDatabase instance;

    public abstract ActivityDAO activityDao();

    public static synchronized ActivityDatabase getInstance(Context context) {

        //context.deleteDatabase("activity_database"); // when you have to delete database for testing

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ActivityDatabase.class, "activity_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    // On create database
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute(); // to auto-populate database on first time
        }
    };

    // TODO: as this is deprecated, we should move to the current meta although not that important bc this is for testing
    // TODO: this should setup the database for user's first time use once we start moving away from prod and testing
    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private ActivityDAO activityDAO;

        public PopulateDBAsyncTask(ActivityDatabase activityDB) {
            activityDAO = activityDB.activityDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityDAO.insert(new ActivityEntity("Test Activity 1", "This is a test activity.", R.color.colorAccent, R.drawable.ic_launcher_foreground));
            activityDAO.insert(new ActivityEntity("Test Activity 2", "This is a test activity.", R.color.colorPrimary, R.drawable.ic_launcher_foreground));
            activityDAO.insert(new ActivityEntity("Test Activity 3", "This is a test activity.", R.color.colorPrimaryDark, R.drawable.ic_launcher_foreground));
            activityDAO.insert(new ActivityEntity("Test Activity 4", "This is a test activity.", R.color.colorAccent, R.drawable.ic_launcher_foreground));
            return null;
        }
    }
}
