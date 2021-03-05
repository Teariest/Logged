package com.clementf.logged.activity_editing;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.clementf.logged.R;


public class ItemSelectorFragment extends DialogFragment {

    private ActivityEditorViewModel viewModel;

    public final static int ICON_ITEM = 0;
    public final static int COLOR_ITEM = 1;
    private int itemType;

    public ItemSelectorFragment() {
        // Required empty public constructor
    }

    public static ItemSelectorFragment newInstance(int itemType) {
        ItemSelectorFragment fragment = new ItemSelectorFragment();
        if (itemType != ICON_ITEM && itemType != COLOR_ITEM) {
            throw new IllegalArgumentException("ItemAdapter.setResourceList(int itemType): parameter itemType is invalid");
        }
        fragment.itemType = itemType;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_selector, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(ActivityEditorViewModel.class);

        RecyclerView recyclerView = getView().findViewById(R.id.item_selector_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        recyclerView.setHasFixedSize(true); // This signifies the recyclerView won't change size, may not be what we want

        ItemAdapter adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private int[] resourceList;

        public ItemAdapter() {
            setResourceList();
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (itemType == ICON_ITEM) {
                holder.imageView.setImageResource(resourceList[position]);
                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.setIconResource(resourceList[position]);
                        dismiss();
                    }
                });
            } else if (itemType == COLOR_ITEM) {
                holder.imageView.setColorFilter(ResourcesCompat.getColor(getResources(), resourceList[position], null), PorterDuff.Mode.SRC_IN);
                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.setColorResource(resourceList[position]);
                        dismiss();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return resourceList.length;
        }

        public void setResourceList() {

            // TODO: get a bunch of icons and put them here
            if (itemType == ICON_ITEM) {
                resourceList = new int[] {
                        R.drawable.ic_baseline_add_24,
                        R.drawable.ic_baseline_circle_24,
                        R.drawable.ic_baseline_edit_24,
                        R.drawable.ic_baseline_insert_emoticon_24
                };
            } else if (itemType == COLOR_ITEM) {
                resourceList = new int[] {
                        R.color.colorAccent,
                        R.color.red,
                        R.color.green,
                        R.color.blue
                };
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private CardView parent;
            private ImageView imageView;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                parent = (CardView) itemView;
                imageView = (ImageView) itemView.findViewById(R.id.item_layout_image_view);
            }
        }
    }
}