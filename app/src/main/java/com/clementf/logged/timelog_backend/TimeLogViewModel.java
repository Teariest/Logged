package com.clementf.logged.timelog_backend;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.clementf.logged.ActivityTimeLog;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeLogViewModel extends AndroidViewModel {

    private static final String TAG = "TimeLogViewModel";

    public TimeLogViewModel(@NonNull Application application) {
        super(application);

        timeLogRepository = TimeLogRepository.getInstance(application);
        timeLogs = timeLogRepository.getAllTimeLogs();
    }

    private TimeLogRepository timeLogRepository;
    private LiveData<List<TimeLogEntity>> timeLogs;

    public void insert(TimeLogEntity timeLog) { timeLogRepository.insert(timeLog); }

    public void delete(TimeLogEntity timeLog) { timeLogRepository.delete(timeLog); }

    public LiveData<List<TimeLogEntity>> getAllTimeLogs() { return timeLogs; }

    private ArrayList<ActivityTimeLog> timeLogData;
    private long span;

    // sets up timeLogData so that the data can be efficiently retrieved when filters are changed
    // also sets up span
    public void setupData() {

        List<TimeLogEntity> logs = timeLogs.getValue();
        if (logs == null || logs.size() == 0) { timeLogData = null; return; } // If there are no time logs

        timeLogData = new ArrayList<>();

        List<Integer> ids = new ArrayList<>();

        long start = logs.get(0).getStartTime();
        long current = Calendar.getInstance().getTimeInMillis();
        span = current - start;

        // Times in milliseconds
        long day = Math.subtractExact(current, 86400000L);
        long week = Math.subtractExact(current, day*7);
        long month = Math.subtractExact(current, day*30);
        long year = Math.subtractExact(current, day*365);

        for (int i = 0; i < logs.size(); i++) {

            int id = logs.get(i).getActivityID();

            if (!ids.contains(id)) { // If we have not made an entry for this ID create the necessary entries
                ids.add(id);
                timeLogData.add(new ActivityTimeLog(id, null));
            }

            long[] timeLogDistribution = new long[5];

            long logStart = logs.get(i).getStartTime();
            long logEnd = i+1 == logs.size()
                    ? current // If final log
                    : logs.get(i + 1).getStartTime(); // Else
            long logSpan = logEnd - logStart;

            if (logStart < year) { // All
                timeLogDistribution[4] = logEnd > year ? Math.subtractExact(year, logStart) : Math.subtractExact(logEnd, logStart);
                logStart = year;
            }
            if (logStart < month && logEnd > year) { // Year
                timeLogDistribution[3] = logEnd > month ? Math.subtractExact(month, logStart) : Math.subtractExact(logEnd, logStart);
                logStart = month;
            }
            if (logStart < week && logEnd > month) { // Month
                timeLogDistribution[2] = logEnd > week ? Math.subtractExact(week, logStart) : Math.subtractExact(logEnd, logStart);
                logStart = week;
            }
            if (logStart < day && logEnd > week) { // Week
                timeLogDistribution[1] = logEnd > day ? Math.subtractExact(day, logStart) : Math.subtractExact(logEnd, logStart);
                logStart = day;
            }
            if (logStart >= day) { // Day
                timeLogDistribution[0] = Math.subtractExact(logEnd, logStart);
            }

            // Now we add the timeLogDistribution of this particular log and add it to the timeLogData

            timeLogData.get(ids.indexOf(id)).addTimeSpent(timeLogDistribution);
        }
    }

    // setupData must be called before getPieData
    // quickly retrieves the data for a pie chart
    public PieData getPieData() {

        if (timeLogData == null || timeLogData.size() == 0 || filter == -1) { // If there's no data or filter set return nothing
            return null;
        }

        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < timeLogData.size(); i++) {

            long sum = 0L;
            long[] timeSpent = timeLogData.get(i).getTimeSpent();

            switch (filter) {
                case ALL_FILTER: sum += timeSpent[ALL_FILTER];
                case YEAR_FILTER: sum += timeSpent[YEAR_FILTER];
                case MONTH_FILTER: sum += timeSpent[MONTH_FILTER];
                case WEEK_FILTER: sum += timeSpent[WEEK_FILTER];
                case DAY_FILTER: sum += timeSpent[DAY_FILTER];
                break;
                default: break;
            }

            float percentage = (float) ((double)sum / span);

            entries.add(new PieEntry(percentage));
        }

        return new PieData(new PieDataSet(entries, "Activity Percentages"));
    }

    private int filter = -1;
    public static final int DAY_FILTER = 0;
    public static final int WEEK_FILTER = 1;
    public static final int MONTH_FILTER= 2;
    public static final int YEAR_FILTER= 3;
    public static final int ALL_FILTER= 4;

    public void setTimeRangeFilter(int filter) {
        if ( 0 <= filter && filter <= 4) {
            this.filter = filter;
        }
    }

    public int getTimeRangeFilter() {
        return filter;
    }
}
