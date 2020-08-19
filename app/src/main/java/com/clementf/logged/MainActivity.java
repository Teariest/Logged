package com.clementf.logged;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.clementf.logged.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager  = findViewById(R.id.view_pager);

        // add fragments to tabLayoutAdapter & add adapter to viewPager
        TabLayoutAdapter tabLayoutAdapter = new TabLayoutAdapter(this);
        tabLayoutAdapter.createFragment(0);
        tabLayoutAdapter.createFragment(1);
        viewPager.setAdapter(tabLayoutAdapter);

        // add mediator that links tabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("Tab " + (position + 1))
        ).attach();

    }

    // TabLayoutAdapter to adapt fragment into ViewPager2
    private class TabLayoutAdapter extends FragmentStateAdapter {

        private int itemCount = 0;

        public TabLayoutAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment;
            switch (position) {
                case 0: fragment = ActivitySelectorFragment.newInstance();
                    break;
                case 1: fragment = DataDisplayFragment.newInstance();
                    break;
                default: throw new IllegalArgumentException("TabLayoutAdapter: createFragment(int position): incorrect position");
            }
            itemCount++;
            return fragment;
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }
    }
}