package com.thiagoalexb.dev.clockin.ui.dayschedules;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.databinding.FragmentDaySchedulesBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleAdapter;

import javax.inject.Inject;

public class DaySchedulesFragment extends BaseFragment {

    @Inject
    public ViewModelProviderFactory modelProviderFactory;
    public EditScheduleViewModel editScheduleViewModel;
    private FragmentDaySchedulesBinding fragmentDaySchedulesBinding;
    private Integer scheduleId;

    public DaySchedulesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        editScheduleViewModel = ViewModelProviders.of(this, modelProviderFactory).get(EditScheduleViewModel.class);

        fragmentDaySchedulesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_day_schedules, container, false);

        checkBundle();

        setObservers();

        return fragmentDaySchedulesBinding.getRoot();
    }

    private void setObservers() {
        editScheduleViewModel.getSchedule().removeObservers(getViewLifecycleOwner());
        editScheduleViewModel.getSchedule().observe(getViewLifecycleOwner(), schedule -> {
            if(schedule == null) return;
            setElements(schedule);
        });
    }

    private void checkBundle(){
        Bundle bundle = getArguments();

        if(bundle != null && bundle.containsKey(ScheduleAdapter.ID_KEY)){
            scheduleId = bundle.getInt(ScheduleAdapter.ID_KEY);
            editScheduleViewModel.checkSchedule(scheduleId);
        }
    }

    private void setElements(Schedule schedule){
        fragmentDaySchedulesBinding.schedulesViewPager.setAdapter(createDaySchedulesAdapter(schedule));
        new TabLayoutMediator(fragmentDaySchedulesBinding.tabsTabLayout, fragmentDaySchedulesBinding.schedulesViewPager,
                (tab, position) -> {
                    if(position == 0)
                        tab.setText(R.string.entry);
                    else
                        tab.setText(R.string.departure);
                }).attach();
    }

    private DaySchedulesAdapter createDaySchedulesAdapter(Schedule schedule) {
        DaySchedulesAdapter adapter = new DaySchedulesAdapter(this, schedule);
        return adapter;
    }




}
