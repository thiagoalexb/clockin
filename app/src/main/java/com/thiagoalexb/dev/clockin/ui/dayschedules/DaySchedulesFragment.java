package com.thiagoalexb.dev.clockin.ui.dayschedules;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.thiagoalexb.dev.clockin.R;

public class DaySchedulesFragment extends Fragment {


    public DaySchedulesFragment() {
    }

    DaySchedulesAdapter daySchedulesAdapter;
    ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_schedules, container, false);
        daySchedulesAdapter = new DaySchedulesAdapter(this);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(daySchedulesAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();

        return view;
    }

}
