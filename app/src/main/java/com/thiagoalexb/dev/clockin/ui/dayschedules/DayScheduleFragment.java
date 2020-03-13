package com.thiagoalexb.dev.clockin.ui.dayschedules;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.databinding.FragmentDayScheduleBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.inject.Inject;

public class DayScheduleFragment extends BaseFragment {

    private FragmentDayScheduleBinding fragmentDayScheduleBinding;
    private static final String ARG_SCHEDULES = "ARG_SCHEDULES";
    private static final String ARG_SCHEDULE_ID = "ARG_SCHEDULE_ID";
    private static final String ARG_IS_ENTRY = "ARG_IS_ENTRY";
    private ArrayList<String> schedules;
    private int scheduleId;
    private boolean isEntry;

    public DayScheduleFragment() {
    }

    public static DayScheduleFragment newInstance(ArrayList<String> schedules, int scheduleId, boolean isEntry) {
        
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_SCHEDULES, schedules);
        args.putInt(ARG_SCHEDULE_ID, scheduleId);
        args.putBoolean(ARG_SCHEDULE_ID, isEntry);
        DayScheduleFragment fragment = new DayScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            schedules = getArguments().getStringArrayList(ARG_SCHEDULES);
            scheduleId = getArguments().getInt(ARG_SCHEDULE_ID);
            isEntry = getArguments().getBoolean(ARG_IS_ENTRY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentDayScheduleBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_day_schedule, container, false);

        DayScheduleAdapter dayScheduleAdapter = new DayScheduleAdapter();

        fragmentDayScheduleBinding.dayScheduleRecyclerView.setAdapter(dayScheduleAdapter);

        dayScheduleAdapter.setSchedules(schedules);
        dayScheduleAdapter.setArguments(scheduleId, isEntry);

        return fragmentDayScheduleBinding.getRoot();
    }
}
