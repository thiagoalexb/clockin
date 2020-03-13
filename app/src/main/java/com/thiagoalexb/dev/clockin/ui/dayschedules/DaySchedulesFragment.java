package com.thiagoalexb.dev.clockin.ui.dayschedules;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.databinding.FragmentDaySchedulesBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleAdapter;

import javax.inject.Inject;

public class DaySchedulesFragment extends BaseFragment {

    public DaySchedulesFragment() {
    }

    @Inject
    public ViewModelProviderFactory modelProviderFactory;
    public EditScheduleViewModel editScheduleViewModel;
    private FragmentDaySchedulesBinding fragmentDaySchedulesBinding;
    private DayScheduleAdapter dayEntryScheduleAdapter;
    private DayScheduleAdapter dayDepartureScheduleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        editScheduleViewModel = ViewModelProviders.of(this, modelProviderFactory).get(EditScheduleViewModel.class);

        fragmentDaySchedulesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_day_schedules, container, false);

        checkBundle();

        setSubscribes();

        setElements();

        return fragmentDaySchedulesBinding.getRoot();
    }

    private void checkBundle(){
        Bundle bundle = getArguments();

        if(bundle == null) return;
        if(bundle.containsKey(ScheduleAdapter.ID_KEY))
            editScheduleViewModel.checkSchedule(bundle.getInt(ScheduleAdapter.ID_KEY));
    }

    private void setSubscribes() {

        editScheduleViewModel.getSchedule().removeObservers(getViewLifecycleOwner());
        editScheduleViewModel.getSchedule().observe(getViewLifecycleOwner(), schedule -> {
            dayEntryScheduleAdapter.setSchedules(schedule.getEntryTimes());
            dayDepartureScheduleAdapter.setSchedules(schedule.getDepartureTimes());
        });
    }

    private void setElements(){

        dayEntryScheduleAdapter = new DayScheduleAdapter(editScheduleViewModel);

        fragmentDaySchedulesBinding.entriesRecyclerView.setAdapter(dayEntryScheduleAdapter);

        dayDepartureScheduleAdapter = new DayScheduleAdapter(editScheduleViewModel);

        fragmentDaySchedulesBinding.departuresRecyclerView.setAdapter(dayDepartureScheduleAdapter);
    }
}
