package com.clementf.logged;

import com.clementf.logged.activity_backend.ActivityEntity;

public class ActivityTimeLog {

    private int activityID;

    // An array of 5 values that contains the time spent for the day, week, month, year, all-time.
    // Week does not contains day and so on, however week+day = actual time spent during the week.
    private long[] timeSpent;

    public ActivityTimeLog (int activityID, long[] timeSpent) {
        if (timeSpent == null) {
            timeSpent = new long[5];
        }
        if (timeSpent.length != 5 || activityID < 0) {
            throw new IllegalArgumentException("ActivityTimeLog must receive a valid activityID and array of timeSpent");
        }
        this.activityID = activityID;
        this.timeSpent = timeSpent;
    }

    public void addTimeSpent(long[] timeSpent) {
        if (timeSpent == null || timeSpent.length != 5) {
            throw new IllegalArgumentException("ActivityTimeLog:addTimeSpent must receive a array of timeSpent");
        }
        for (int i = 0; i < timeSpent.length; i++) {
            this.timeSpent[i] += timeSpent[i];
        }
    }

    public int getActivity() {
        return activityID;
    }

    public long[] getTimeSpent() {
        return timeSpent;
    }
}
