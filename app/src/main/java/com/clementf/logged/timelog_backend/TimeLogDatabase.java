package com.clementf.logged.timelog_backend;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.clementf.logged.R;
import com.clementf.logged.activity_backend.ActivityDAO;
import com.clementf.logged.activity_backend.ActivityDatabase;
import com.clementf.logged.activity_backend.ActivityEntity;

@Database(entities = TimeLogEntity.class, version = 1)
public abstract class TimeLogDatabase extends RoomDatabase {

    private static TimeLogDatabase instance;

    public abstract TimeLogDAO timeLogDAO();

    public static synchronized TimeLogDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TimeLogDatabase.class, "timelog_database")
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
}
