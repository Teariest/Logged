package com.clementf.logged.timelog_backend;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.util.Date;
import java.util.TimeZone;

@Entity(tableName = "timelog_table")
public class TimeLogEntity {

    @PrimaryKey
    @ColumnInfo(name = "timelog")
    private long startTime;
    private int activityID;
    private int timeZoneOffset;

    public TimeLogEntity(Date startTime, int activityID, int timeZoneOffset) {
        this.startTime = startTime.getTime();
        this.activityID = activityID;
        this.timeZoneOffset = timeZoneOffset;
    }

    public TimeLogEntity(long startTime, int activityID, int timeZoneOffset) {
        this.startTime = startTime;
        this.activityID = activityID;
        this.timeZoneOffset = timeZoneOffset;
    }

    public Date getStartTimeAsDate() {
        return new Date(startTime);
    }

    public long getStartTime() {
        return startTime;
    }

    public int getActivityID() {
        return activityID;
    }

    public int getTimeZoneOffset() { return timeZoneOffset; }
}
