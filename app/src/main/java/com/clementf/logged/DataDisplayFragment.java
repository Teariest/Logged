package com.clementf.logged;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clementf.logged.R;
import com.clementf.logged.activity_backend.ActivityEntity;
import com.clementf.logged.timelog_backend.TimeLogEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DataDisplayFragment extends Fragment {

    private DataDisplayViewModel viewModel;

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

        RecyclerView recyclerView = getView().findViewById(R.id.data_display_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true); // This signifies the recyclerView won't change size, may not be what we want

        TimeLogAdapter adapter = new TimeLogAdapter();
        recyclerView.setAdapter(adapter);

        // Setup DataDisplayViewModel
        viewModel = new ViewModelProvider(getActivity()).get(DataDisplayViewModel.class);
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
}