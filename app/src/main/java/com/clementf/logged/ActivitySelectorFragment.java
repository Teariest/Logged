package com.clementf.logged;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clementf.logged.R;

import java.util.List;

public class ActivitySelectorFragment extends Fragment {

    private ActivityViewModel activityViewModel;

    public ActivitySelectorFragment() {
        // Required empty public constructor
    }

    public static ActivitySelectorFragment newInstance() {
        return new ActivitySelectorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // setup view model
        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        activityViewModel.getAllActivities().observe(this, new Observer<List<ActivityEntity>>() {
            @Override
            public void onChanged(List<ActivityEntity> activityEntities) {
                // update Recycler View
                // this may have to be done in fragment
            }
        });

         
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_selector, container, false);
    }
}