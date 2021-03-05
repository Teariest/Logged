package com.clementf.logged;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clementf.logged.R;
import com.clementf.logged.activity_backend.ActivityEntity;
import com.clementf.logged.activity_backend.ActivityViewModel;
import com.clementf.logged.timelog_backend.TimeLogEntity;
import com.clementf.logged.timelog_backend.TimeLogViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static java.util.Calendar.getInstance;


public class DataDisplayFragment extends Fragment {

    private static final String TAG = "Data Display Fragment";

    private TimeLogViewModel timeLogViewModel;
    private ActivityViewModel activityViewModel;

    public DataDisplayFragment() {
        // Required empty public constructor
    }

    public static DataDisplayFragment newInstance() {
        return new DataDisplayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_display, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        timeLogViewModel = new ViewModelProvider(getActivity()).get(TimeLogViewModel.class);
        activityViewModel = new ViewModelProvider(getActivity()).get(ActivityViewModel.class);

        // Setup Menu
        setupMenu();

        // Will display the graphs
        displayData();


        // WILL PRINT THE LOG ON THE APP IN PLAIN TEXT
        //printLog();

    }

    // Setup Menu
    private void setupMenu() {

        BottomAppBar bottomAppBar = getView().findViewById(R.id.bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DrawerLayout) getView().findViewById(R.id.drawerLayout)).openDrawer(GravityCompat.START);
            }
        });

        NavigationView navDrawer = getView().findViewById(R.id.nav_drawer);

        navDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: --");
                switch (item.getItemId()) {
                    case R.id.time_filter_all: timeLogViewModel.setTimeRangeFilter(TimeLogViewModel.ALL_FILTER);
                        break;
                    case R.id.time_filter_year: timeLogViewModel.setTimeRangeFilter(TimeLogViewModel.YEAR_FILTER);
                        break;
                    case R.id.time_filter_month: timeLogViewModel.setTimeRangeFilter(TimeLogViewModel.MONTH_FILTER);
                        break;
                    case R.id.time_filter_week: timeLogViewModel.setTimeRangeFilter(TimeLogViewModel.WEEK_FILTER);
                        break;
                    case R.id.time_filter_day: timeLogViewModel.setTimeRangeFilter(TimeLogViewModel.DAY_FILTER);
                        break;

                    case R.id.graph_type_pie: displayPieGraph();
                        break;
                    case R.id.graph_type_line: displayLineGraph();
                        break;
                    case R.id.graph_type_calendar: displayCalendarView();
                        break;

                    default: return false;
                }
                return true;
            }
        });

        navDrawer.setCheckedItem(R.id.graph_type_pie);
        navDrawer.setCheckedItem(R.id.time_filter_all);
    }

    private int visibleDisplayID = R.id.pie_chart; // ID of chart currently displayed

    // To display the data for the user
    // TODO: remove
    private void displayData() {

        timeLogViewModel.getAllTimeLogs().observe(getViewLifecycleOwner(), new Observer<List<TimeLogEntity>>() {
            @Override
            public void onChanged(List<TimeLogEntity> timeLogEntities) {

                Log.d(TAG, "onChanged: --");

                timeLogViewModel.setupData();

                switch (visibleDisplayID) {
                    case R.id.pie_chart: displayPieGraph();
                        break;
                    case R.id.line_chart: displayLineGraph();
                        break;
                    default: throw new IllegalArgumentException("Invalid visibleDisplayID.");
                }
            }
        });
    }

    private void displayPieGraph() {

        // Setup visibility stuff
        if (visibleDisplayID != R.id.pie_chart) {
            getView().findViewById(visibleDisplayID).setVisibility(View.INVISIBLE);
            visibleDisplayID = R.id.pie_chart;
        }

        PieChart pieChart = getView().findViewById(R.id.pie_chart);
        pieChart.setVisibility(View.VISIBLE);

        // Setup data
        PieData timeLogData = timeLogViewModel.getPieData();
        if (timeLogData == null) {
            Log.d(TAG, "displayPieGraph: No data to be displayed.");
            return;
        }

        Log.d(TAG, "displayPieGraph: ");
        pieChart.setData(timeLogData);
        pieChart.invalidate();
    }

    private void displayLineGraph() {
        if (visibleDisplayID != R.id.line_chart) {
            getView().findViewById(visibleDisplayID).setVisibility(View.INVISIBLE);
            visibleDisplayID = R.id.line_chart;
        }

        LineChart lineChart = getView().findViewById(R.id.line_chart);
        lineChart.setVisibility(View.VISIBLE);

        // TODO: get data and setup chart
    }

    private void displayCalendarView() {
        // TODO

    }


    /*
    // WILL PRINT THE LOG ON THE APP IN PLAIN TEXT
    private void printLog() {
        // SETUP RECYCLER VIEW FOR DEBUGGING
        RecyclerView recyclerView = getView().findViewById(R.id.data_display_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true); // This signifies the recyclerView won't change size, may not be what we want

        TimeLogAdapter adapter = new TimeLogAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.getAllTimeLogs().observe(getViewLifecycleOwner(), new Observer<List<TimeLogEntity>>() {
            @Override
            public void onChanged(List<TimeLogEntity> timeLogEntities) {
                adapter.setTimeLogs(timeLogEntities);
            }
        });
    }

    public class TimeLogAdapter extends RecyclerView.Adapter<TimeLogAdapter.TimeLogViewHolder> {

        private List<TimeLogEntity> timeLogs = new ArrayList<>();

        @NonNull
        @Override
        public TimeLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.timelog_item, parent, false);
            return new TimeLogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TimeLogViewHolder holder, int position) {
            TimeLogEntity timeLog = timeLogs.get(position);
            String endTime;
            if (timeLogs.size() == position+1) {
                endTime = "now!";
            } else {
                endTime = timeLogs.get(position+1).getStartTimeAsDate().toString();
            }
            String text = "Activity ID: " + timeLog.getActivityID() + " Start Time: " + timeLog.getStartTimeAsDate().toString() + " End Time: " + endTime;
            holder.timeTextView.setText(text);
        }

        @Override
        public int getItemCount() {
            return timeLogs.size();
        }

        public void setTimeLogs(List<TimeLogEntity> timeLogs) {
            this.timeLogs = timeLogs;
            notifyDataSetChanged();
        }

        public class TimeLogViewHolder extends RecyclerView.ViewHolder {

            private TextView timeTextView;

            public TimeLogViewHolder(@NonNull View itemView) {
                super(itemView);
                timeTextView = (TextView) itemView;
            }
        }
    }



     */

}