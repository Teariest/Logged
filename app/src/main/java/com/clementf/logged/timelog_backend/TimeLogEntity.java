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
    private int timeZoneOffset;
    private int activityID;

    public TimeLogEntity(Date startTime, int timeZoneOffset, int activityID) {
        this.startTime = startTime.getTime();
        this.timeZoneOffset = timeZoneOffset;
        this.activityID = activityID;

    }

    public TimeLogEntity(long startTime, int timeZoneOffset, int activityID) {
        this.startTime = startTime;
        this.timeZoneOffset = timeZoneOffset;
        this.activityID = activityID;
    }

    public Date getStartTimeAsDate() {
        return new Date(startTime);
    }

    public long getStartTime() {
        return startTime;
    }

    public int getTimeZoneOffset() { return timeZoneOffset; }

    public int getActivityID() {
        return activityID;
    }

}
