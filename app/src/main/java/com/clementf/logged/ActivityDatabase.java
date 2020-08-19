package com.clementf.logged;

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
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ActivityDatabase.class, "activity_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private ActivityDAO activityDAO;

        public PopulateDBAsyncTask(ActivityDAO activityDAO) {
            this.activityDAO = activityDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityDAO.insert(new ActivityEntity("Test Activity 1", "This is a test activity.", R.color.colorAccent, R.drawable.ic_launcher_foreground, 0));
            activityDAO.insert(new ActivityEntity("Test Activity 2", "This is a test activity.", R.color.colorPrimary, R.drawable.ic_launcher_foreground, 1));
            activityDAO.insert(new ActivityEntity("Test Activity 3", "This is a test activity.", R.color.colorPrimaryDark, R.drawable.ic_launcher_foreground, 2));
            activityDAO.insert(new ActivityEntity("Test Activity 4", "This is a test activity.", R.color.colorAccent, R.drawable.ic_launcher_foreground, 3));
            return null;
        }
    }
}
