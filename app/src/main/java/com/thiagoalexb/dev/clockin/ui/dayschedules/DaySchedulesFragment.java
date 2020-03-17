package com.thiagoalexb.dev.clockin.ui.dayschedules;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.TypeSchedule;
import com.thiagoalexb.dev.clockin.databinding.FragmentDaySchedulesBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.ui.animation.ViewAnimation;
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleAdapter;
import com.thiagoalexb.dev.clockin.util.RecyclerTouchListener;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.inject.Inject;

public class DaySchedulesFragment extends BaseFragment {

    public DaySchedulesFragment() {
    }

    @Inject
    public ViewModelProviderFactory modelProviderFactory;
    public DaySchedulesViewModel daySchedulesViewModel;
    private FragmentDaySchedulesBinding fragmentDaySchedulesBinding;
    private DaySchedulesAdapter dayEntryScheduleAdapter;
    private DaySchedulesAdapter dayDepartureScheduleAdapter;
    private int scheduleId;
    private Boolean isRotate = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        daySchedulesViewModel = ViewModelProviders.of(this, modelProviderFactory).get(DaySchedulesViewModel.class);

        fragmentDaySchedulesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_day_schedules, container, false);

        checkBundle();

        setSubscribes();

        setElements();

        return fragmentDaySchedulesBinding.getRoot();
    }

    private void checkBundle() {
        Bundle bundle = getArguments();

        if (bundle == null) return;
        if (bundle.containsKey(ScheduleAdapter.ID_KEY)) {
            scheduleId = bundle.getInt(ScheduleAdapter.ID_KEY);
            daySchedulesViewModel.checkSchedule(scheduleId);
        }
    }

    private void setSubscribes() {

        daySchedulesViewModel.getSchedule().removeObservers(getViewLifecycleOwner());
        daySchedulesViewModel.getSchedule().observe(getViewLifecycleOwner(), schedule -> {
            if (schedule == null) return;

            dayEntryScheduleAdapter.setArguments(schedule.getEntryTimes(), TypeSchedule.ENTRY);
            dayDepartureScheduleAdapter.setArguments(schedule.getDepartureTimes(), TypeSchedule.DEPARTURE);
        });

        daySchedulesViewModel.getStatusInsert().removeObservers(getViewLifecycleOwner());
        daySchedulesViewModel.getStatusInsert().observe(getViewLifecycleOwner(), status -> {
            if (status == null) return;

            daySchedulesViewModel.checkSchedule(scheduleId);
        });

        daySchedulesViewModel.getScheduleResource().removeObservers(getViewLifecycleOwner());
        daySchedulesViewModel.getScheduleResource().observe(getViewLifecycleOwner(), resource -> {
            setLoading(resource.status);
        });
    }

    private void setElements() {

        dayEntryScheduleAdapter = new DaySchedulesAdapter(this);
        setRecyclerView(fragmentDaySchedulesBinding.entriesRecyclerView, dayEntryScheduleAdapter, TypeSchedule.ENTRY);

        dayDepartureScheduleAdapter = new DaySchedulesAdapter(this);
        setRecyclerView(fragmentDaySchedulesBinding.departuresRecyclerView, dayDepartureScheduleAdapter, TypeSchedule.DEPARTURE);

        setFabButtons();

    }

    private void setRecyclerView(RecyclerView recyclerView, DaySchedulesAdapter dayDepartureScheduleAdapter, TypeSchedule typeSchedule) {
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(dayDepartureScheduleAdapter);

        RecyclerTouchListener touchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        touchListener.openSwipeOptions(position);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.edit_image_view, R.id.delete_image_view)
                .setTypeSchedule(typeSchedule)
                .setSwipeable(R.id.container_schedule, R.id.container_actions, (viewID, position) -> {
                    switch (viewID) {
                        case R.id.delete_image_view:
                            Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.edit_image_view:
                            setTimePickerDialog(getView(), touchListener.getTypeSchedule());
                            break;

                    }
                });

        recyclerView.addOnItemTouchListener(touchListener);
    }

    private void setFabButtons() {
        ViewAnimation.init(fragmentDaySchedulesBinding.entryFloatingActionButton, fragmentDaySchedulesBinding.entryFloatingTextView);
        ViewAnimation.init(fragmentDaySchedulesBinding.departureFloatingActionButton, fragmentDaySchedulesBinding.departureFloatingTextView);

        fragmentDaySchedulesBinding.actionsFloatingActionButton.setOnClickListener(view -> {
            setAnimationActionsFabButton();
        });

        fragmentDaySchedulesBinding.entryFloatingActionButton.setOnClickListener(view -> {
            setAnimationActionsFabButton();
            setTimePickerDialog(view, TypeSchedule.ENTRY);
        });

        fragmentDaySchedulesBinding.departureFloatingActionButton.setOnClickListener(view -> {
            setAnimationActionsFabButton();
            setTimePickerDialog(view, TypeSchedule.DEPARTURE);
        });
    }

    private void setTimePickerDialog(View view, final TypeSchedule typeSchedule) {
        LocalDateTime now = LocalDateTime.now();
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), (timePicker, hourOfDay, minute) -> {
            LocalDateTime newTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), hourOfDay, minute);
            if (typeSchedule == TypeSchedule.ENTRY)
                daySchedulesViewModel.insertEntry(newTime);
            else
                daySchedulesViewModel.insertDeparture(newTime);
        }, now.getHour(), now.getMinute(), true);

        timePickerDialog.show();
    }

    private void setAnimationActionsFabButton() {
        isRotate = ViewAnimation.rotateFab(fragmentDaySchedulesBinding.actionsFloatingActionButton, !isRotate);

        if (isRotate) {
            ViewAnimation.showIn(fragmentDaySchedulesBinding.entryFloatingActionButton, fragmentDaySchedulesBinding.entryFloatingTextView);
            ViewAnimation.showIn(fragmentDaySchedulesBinding.departureFloatingActionButton, fragmentDaySchedulesBinding.departureFloatingTextView);
        } else {
            ViewAnimation.showOut(fragmentDaySchedulesBinding.entryFloatingActionButton, fragmentDaySchedulesBinding.entryFloatingTextView);
            ViewAnimation.showOut(fragmentDaySchedulesBinding.departureFloatingActionButton, fragmentDaySchedulesBinding.departureFloatingTextView);
        }
    }
}
