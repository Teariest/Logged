package com.clementf.logged;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.clementf.logged.activity_backend.ActivityEntity;

import java.util.List;

public class ActivityEditorFragment extends DialogFragment {

    private static final String TAG = "ActivityEditorFragment";

    private ActivitySelectorViewModel viewModel;
    private int activityID;

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
        return inflater.inflate(R.layout.fragment_activity_editor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ActivitySelectorViewModel.class);
        viewModel.getAllActivities().observe(getViewLifecycleOwner(), new Observer<List<ActivityEntity>>() {
            @Override
            public void onChanged(List<ActivityEntity> activityEntities) {
                populateViews(activityEntities.get(viewModel.getIndexFromActivityID(activityID)));
            }
        });
    }

    // Populate views with correct resources for the activityEntity
    private void populateViews(ActivityEntity activityEntity) {

        View parent = getView();
        ((EditText)parent.findViewById(R.id.edit_fragment_title_editText)).setText(activityEntity.getTitle());
        ((EditText)parent.findViewById(R.id.edit_fragment_description_editText)).setText(activityEntity.getDescription());

        /* Here we use the ViewModel and liveData to populate the views
            We add an observer so that when IconResource liveData is changed, the icons in the views change too
            We set the icon resources in the ViewModel to the current activityEntity icon, which triggers the observer and populates the views with the starting variables.
        */
        viewModel.getIconResource().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer resource) {
                Log.d("POPULATEVIEWS", "it happened");
                ((ImageView)parent.findViewById(R.id.edit_fragment_icon_imageView)).setImageResource(resource);
                ((ImageView)parent.findViewById(R.id.edit_fragment_icon_imageView)).setTag(resource);
            }
        });
        viewModel.setIconResource(activityEntity.getIcon());

        // Same as above but with colors
        viewModel.getColorResource().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer resource) {
                ((ImageView)parent.findViewById(R.id.edit_fragment_color_imageView)).setColorFilter(resource);
                ((ImageView)parent.findViewById(R.id.edit_fragment_color_imageView)).setTag(resource);
            }
        });
        viewModel.setColorResource(activityEntity.getColor());

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
                ActivityEntity activityEntity = new ActivityEntity(
                        ((EditText)parent.findViewById(R.id.edit_fragment_title_editText)).getText().toString(),
                        ((EditText)parent.findViewById(R.id.edit_fragment_description_editText)).getText().toString(),
                        (int)((ImageView)parent.findViewById(R.id.edit_fragment_icon_imageView)).getTag(),
                        (int)((ImageView)parent.findViewById(R.id.edit_fragment_color_imageView)).getTag()
                );
                activityEntity.setId(activityID);
                viewModel.update(activityEntity);
                dismiss();
            }
        });
    }
}