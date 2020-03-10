package com.thiagoalexb.dev.clockin.ui.dayschedules;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DaySchedulesAdapter extends FragmentStateAdapter {

    public DaySchedulesAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = new DayScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(DayScheduleFragment.ARG_OBJECT, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 100;
    }
}
