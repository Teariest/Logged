package com.clementf.logged;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;


public class ActivityCreatorFragment extends DialogFragment {

    public ActivityCreatorFragment() {
        // Required empty public constructor
    }

    public static ActivityCreatorFragment newInstance() {
        ActivityCreatorFragment fragment = new ActivityCreatorFragment();
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


    }

    // Sets up the layout to match creator
    private void populateViews() {

        View parent = getView();

        ((TextView) parent.findViewById(R.id.edit_fragment_fragment_title)).setText(R.string.activity_creator_layout_title);

        ((EditText) parent.findViewById(R.id.edit_fragment_title_editText)).setHint(R.string.activity_creator_default_title);
        ((EditText) parent.findViewById(R.id.edit_fragment_description_editText)).setHint(R.string.activity_creator_default_description);

        ((ImageView) parent.findViewById(R.id.edit_fragment_icon_imageView)).setImageResource(R.drawable.ic_baseline_insert_emoticon_24);
        ((ImageView) parent.findViewById(R.id.edit_fragment_color_imageView)).setColorFilter(R.color.colorAccent);

        ((MaterialButton) parent.findViewById(R.id.edit_fragment_save_button)).setText(R.string.activity_creator_create);
    }
}