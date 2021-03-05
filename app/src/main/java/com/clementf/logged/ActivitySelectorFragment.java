package com.clementf.logged;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.clementf.logged.activity_backend.ActivityEntity;
import com.clementf.logged.activity_backend.ActivityViewModel;
import com.clementf.logged.activity_editing.ActivityEditorFragment;
import com.clementf.logged.timelog_backend.TimeLogEntity;
import com.clementf.logged.timelog_backend.TimeLogViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ActivitySelectorFragment extends Fragment {

    private ActivityViewModel activityViewModel;
    private TimeLogViewModel timeLogViewModel;

    public ActivitySelectorFragment() {
        // Required empty public constructor
    }

    public static ActivitySelectorFragment newInstance() {
        return new ActivitySelectorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_selector, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.activity_selector_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true); // This signifies the recyclerView won't change size, may not be what we want

        ActivityAdapter adapter = new ActivityAdapter();
        recyclerView.setAdapter(adapter);

        // Setup ActivityViewModel NOTE: we pass in getActivity to viewModelProvider bc we want to use Activity Lifecycle
        activityViewModel = new ViewModelProvider(getActivity()).get(ActivityViewModel.class);
        timeLogViewModel = new ViewModelProvider(getActivity()).get(TimeLogViewModel.class);

        // here we use getViewLifecycleOwner to avoid getting redundant calls when fragment view lifecycle gets re-built
        activityViewModel.getAllActivities().observe(getViewLifecycleOwner(), new Observer<List<ActivityEntity>>() {
            @Override
            public void onChanged(List<ActivityEntity> activityEntities) {
                adapter.setActivities(activityEntities);
            }
        });

        FloatingActionButton fab = getView().findViewById(R.id.activity_selector_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ActivityEditorFragment activityCreatorFragment = ActivityEditorFragment.newInstance(-1);
                activityCreatorFragment.show(fragmentManager, null);
            }
        });
    }

    // for now I want to keep this within this file however I may have to move the class in it's own file
    public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder> {

        private static final String TAG = "ActivityAdapter";

        private List<ActivityEntity> activities = new ArrayList<ActivityEntity>();

        @NonNull
        @Override
        public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_item, parent, false);
            return new ActivityHolder(itemView);
        }

        // setup the views of the viewholder so they display the appropriate values
        @Override
        public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
            ActivityEntity currentActivity = activities.get(position);

            holder.parent.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), currentActivity.getColorResource(), null));
            holder.iconImageView.setImageResource(currentActivity.getIconResource());
            holder.titleTextView.setText(currentActivity.getName());
            holder.descriptionTextView.setText(currentActivity.getDescription());
            // onclick listener to log time
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeLogViewModel.insert(new TimeLogEntity(Calendar.getInstance().getTime(), TimeZone.getDefault().getRawOffset(), currentActivity.getId()));
                }
            });
            // onclick listener to goto EditActivityFragment
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    ActivityEditorFragment activityEditorFragment = ActivityEditorFragment.newInstance(currentActivity.getId());
                    activityEditorFragment.show(fragmentManager, null);
                }
            });
        }

        @Override
        public int getItemCount() {
            return activities.size();
        }

        public void setActivities(List<ActivityEntity> activities) {
            this.activities = activities;
            notifyDataSetChanged();
        }

        public class ActivityHolder extends RecyclerView.ViewHolder {

            private CardView parent;
            private ImageView iconImageView;
            private TextView titleTextView;
            private TextView descriptionTextView;
            private ImageButton editButton;

            public ActivityHolder(@NonNull View itemView) {
                super(itemView);
                parent = (CardView) itemView;
                iconImageView = itemView.findViewById(R.id.activity_item_image);
                titleTextView = itemView.findViewById(R.id.activity_item_title);
                descriptionTextView = itemView.findViewById(R.id.activity_item_description);
                editButton = itemView.findViewById(R.id.activity_item_editbutton);
            }
        }
    }
}