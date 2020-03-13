package com.thiagoalexb.dev.clockin.ui.dayschedules;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thiagoalexb.dev.clockin.data.models.Schedule;

public class DaySchedulesAdapter extends FragmentStateAdapter {

    private static final int CARD_ITEM_SIZE = 2;
    private final Schedule schedule;

    public DaySchedulesAdapter(@NonNull Fragment fragment, Schedule schedule) {
        super(fragment);
        this.schedule = schedule;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        boolean isEntry = position == 0;
        return DayScheduleFragment.newInstance(isEntry ? schedule.getEntryTimes() : schedule.getDepartureTimes(), schedule.getId(), isEntry);
    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}
