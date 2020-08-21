package com.clementf.logged;

import android.os.Bundle;
import android.util.Log;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
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
        tabLayoutAdapter.addFragment(new ActivitySelectorFragment());
        tabLayoutAdapter.addFragment(new DataDisplayFragment());
        viewPager.setAdapter(tabLayoutAdapter);

        // add mediator that links tabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0: tab.setText("Activities");
                                break;
                            case 1: tab.setText("Data");
                                break;
                            default: throw new IllegalArgumentException("Tab at position:" + position + " is not configured in OnConfigureTab.");
                        }
                    }
                }
        ).attach();

    }

    // TabLayoutAdapter to adapt fragment into ViewPager2
    private class TabLayoutAdapter extends FragmentStateAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

        public TabLayoutAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment) { fragments.add(fragment); }
    }
}