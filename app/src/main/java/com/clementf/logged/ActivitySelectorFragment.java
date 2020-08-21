package com.clementf.logged;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
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
import com.clementf.logged.timelog_backend.TimeLogEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ActivitySelectorFragment extends Fragment {

    private ActivitySelectorViewModel viewModel;

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

        // Setup ActivityViewModel
        viewModel = new ViewModelProvider(getActivity()).get(ActivitySelectorViewModel.class);
        viewModel.getAllActivities().observe(getViewLifecycleOwner(), new Observer<List<ActivityEntity>>() {
            @Override
            public void onChanged(List<ActivityEntity> activityEntities) {
                adapter.setActivities(activityEntities);
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
            Log.d(TAG, "onBindViewHolder: position:" + position);
            ActivityEntity currentActivity = activities.get(position);
            holder.parent.setCardBackgroundColor(currentActivity.getColor());
            holder.iconImageView.setImageResource(currentActivity.getIcon());
            holder.titleTextView.setText(currentActivity.getTitle());
            holder.descriptionTextView.setText(currentActivity.getDescription());
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.insert(new TimeLogEntity(Calendar.getInstance().getTime(), currentActivity.getId(), TimeZone.getDefault().getRawOffset()));
                }
            });
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    EditActivityFragment editActivityFragment = EditActivityFragment.newInstance();
                    editActivityFragment.show(fragmentManager, null);
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