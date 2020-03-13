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
import com.thiagoalexb.dev.clockin.data.TypeSchedule;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.databinding.FragmentDayScheduleBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleAdapter;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.sql.Types;
import java.util.ArrayList;

import javax.inject.Inject;

public class DayScheduleFragment extends BaseFragment {

    private static String TYPE_SCHEDULE_KEY = "TYPE_SCHEDULE_KEY";
    private static String SCHEDULE_ID_KEY = "SCHEDULE_ID_KEY";

    @Inject
    public ViewModelProviderFactory modelProviderFactory;
    public EditScheduleViewModel editScheduleViewModel;
    private FragmentDayScheduleBinding fragmentDayScheduleBinding;
    private int scheduleId;
    private TypeSchedule typeSchedule;

    public static DayScheduleFragment newInstance(TypeSchedule typeSchedule, int scheduleId) {

        Bundle args = new Bundle();
        args.putInt(TYPE_SCHEDULE_KEY, typeSchedule.ordinal());
        args.putInt(SCHEDULE_ID_KEY, scheduleId);
        DayScheduleFragment fragment = new DayScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public DayScheduleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle == null) return;

        typeSchedule = TypeSchedule.values()[bundle.getInt(TYPE_SCHEDULE_KEY)];
        scheduleId = bundle.getInt(SCHEDULE_ID_KEY);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        editScheduleViewModel = ViewModelProviders.of(this, modelProviderFactory).get(EditScheduleViewModel.class);

        fragmentDayScheduleBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_day_schedule, container, false);

        editScheduleViewModel.checkSchedule(scheduleId);

        editScheduleViewModel.getSchedule().removeObservers(getViewLifecycleOwner());
        editScheduleViewModel.getSchedule().observe(getViewLifecycleOwner(), schedule -> {
            DayScheduleAdapter dayScheduleAdapter = new DayScheduleAdapter();

            fragmentDayScheduleBinding.dayScheduleRecyclerView.setAdapter(dayScheduleAdapter);

            dayScheduleAdapter.setSchedules(typeSchedule == TypeSchedule.ENTRY ? schedule.getEntryTimes() : schedule.getDepartureTimes());
        });


        return fragmentDayScheduleBinding.getRoot();
    }
}
