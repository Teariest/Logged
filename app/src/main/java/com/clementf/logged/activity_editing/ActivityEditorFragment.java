package com.clementf.logged.activity_editing;

import android.content.res.Resources;
import android.content.res.loader.ResourcesProvider;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.clementf.logged.R;
import com.clementf.logged.activity_backend.ActivityEntity;

public class ActivityEditorFragment extends DialogFragment {

    private static final String TAG = "ActivityEditorFragment";

    private ActivityEditorViewModel viewModel;

    private int activityID;

    public ActivityEditorFragment() {
        // Required empty public constructor
    }

    // Pass in negative activityID to create new fragment
    public static ActivityEditorFragment newInstance(int activityID) {
        ActivityEditorFragment fragment = new ActivityEditorFragment();
        fragment.activityID = activityID;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_editor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(ActivityEditorViewModel.class);

        if (activityID < 0) { // When we want to create a new activity, we setup the VM with an ActivityEntity not yet into the DB
            populateViews(new ActivityEntity(
                    getResources().getString(R.string.activity_creator_default_title),
                    getResources().getString(R.string.activity_creator_default_description),
                    R.color.colorPrimary,
                    R.drawable.ic_baseline_insert_emoticon_24
            ));
        } else {
            viewModel.setCurrentActivity(activityID);
            viewModel.getActivity().observe(getViewLifecycleOwner(), new Observer<ActivityEntity>() {
                @Override
                public void onChanged(ActivityEntity activityEntity) {
                    populateViews(viewModel.getActivity().getValue());
                }
            });
        }
    }

    // Populate views with correct resources for the activityEntity
    private void populateViews(ActivityEntity activityEntity) {

        View parent = getView();
        ((EditText)parent.findViewById(R.id.edit_fragment_title_editText)).setText(activityEntity.getName());
        ((EditText)parent.findViewById(R.id.edit_fragment_description_editText)).setText(activityEntity.getDescription());

        /* Here we use the ViewModel and liveData to populate the views
            We add an observer so that when IconResource liveData is changed, the icons in the views change too
            We set the icon resources in the ViewModel to the current activityEntity icon, which triggers the observer and populates the views with the starting variables.
        */
        Log.d(TAG, "populateViews: ");
        viewModel.getIconResource().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer resource) {
                //Log.d(TAG, "populateViewsiconOnChanged: ");
                ((ImageView)parent.findViewById(R.id.edit_fragment_icon_imageView)).setImageResource(resource);
                ((ImageView)parent.findViewById(R.id.edit_fragment_icon_imageView)).setTag(resource);
            }
        });
        viewModel.setIconResource(activityEntity.getIconResource());

        // Same as above but with colors
        viewModel.getColorResource().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer resource) {
                //Log.d(TAG, "populateViews: onChanged: ");
                ((ImageView)parent.findViewById(R.id.edit_fragment_color_imageView)).setColorFilter(ResourcesCompat.getColor(getResources(), resource, null));
                ((ImageView)parent.findViewById(R.id.edit_fragment_color_imageView)).setTag(resource);
            }
        });
        viewModel.setColorResource(activityEntity.getColorResource());

        ActivityEditorFragment fragment = this;

        // Open icon picker fragment
        parent.findViewById(R.id.edit_fragment_icon_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ItemSelectorFragment itemSelectorFragment = ItemSelectorFragment.newInstance(ItemSelectorFragment.ICON_ITEM);
                itemSelectorFragment.show(fragmentManager, null);
            }
        });

        // Open color picker fragment
        parent.findViewById(R.id.edit_fragment_color_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ItemSelectorFragment itemSelectorFragment = ItemSelectorFragment.newInstance(ItemSelectorFragment.COLOR_ITEM);
                itemSelectorFragment.show(fragmentManager, null);
            }
        });

        // Save activity and close fragment
        parent.findViewById(R.id.edit_fragment_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityID < 0) { // when creating activity
                    viewModel.createAndPublishActivity(
                            ((EditText)parent.findViewById(R.id.edit_fragment_title_editText)).getText().toString(),
                            ((EditText)parent.findViewById(R.id.edit_fragment_description_editText)).getText().toString()
                    );
                } else { // when editing activity
                    viewModel.updateAndPublishActivity(
                            ((EditText)parent.findViewById(R.id.edit_fragment_title_editText)).getText().toString(),
                            ((EditText)parent.findViewById(R.id.edit_fragment_description_editText)).getText().toString()
                    );
                }
                dismiss();
            }
        });
    }
}