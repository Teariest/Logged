package com.clementf.logged;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.clementf.logged.activity_backend.ActivityEntity;

public class ActivityEditorFragment extends DialogFragment {

    private static final String TAG = "ActivityEditorFragment";

    private ActivityEditorViewModel viewModel;;

    private int activityID;
    private ActivityEntity activityEntity;

    public ActivityEditorFragment() {
        // Required empty public constructor
    }

    public static ActivityEditorFragment newInstance(int activityID) {
        ActivityEditorFragment fragment = new ActivityEditorFragment();
        fragment.activityID = activityID;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_activity, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ActivityEditorViewModel.class);
        viewModel.getActivity(activityID).observe(getViewLifecycleOwner(), new Observer<ActivityEntity>() {
            @Override
            public void onChanged(ActivityEntity changedActivityEntity) {
                activityEntity = changedActivityEntity;
                populateViews();
            }
        });
    }

    private void populateViews() {

        View parent = getView();
        ((EditText)parent.findViewById(R.id.edit_fragment_title_editText)).setText(activityEntity.getTitle());
        ((EditText)parent.findViewById(R.id.edit_fragment_description_editText)).setText(activityEntity.getDescription());
        ((ImageView)parent.findViewById(R.id.edit_fragment_icon_imageView)).setImageResource(activityEntity.getIcon());
        ((ImageView)parent.findViewById(R.id.edit_fragment_icon_imageView)).setImageResource(activityEntity.getIcon());

        // Open icon picker fragment
        parent.findViewById(R.id.edit_fragment_icon_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
            }
        });

        // Open color picker fragment
        parent.findViewById(R.id.edit_fragment_color_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
            }
        });

        parent.findViewById(R.id.edit_fragment_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.update(activityEntity);
                dismiss();
            }
        });
    }
}