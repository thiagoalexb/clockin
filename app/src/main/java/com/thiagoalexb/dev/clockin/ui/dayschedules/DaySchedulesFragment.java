package com.thiagoalexb.dev.clockin.ui.dayschedules;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
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
import java.util.Objects;

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
            if (schedule == null) {
                Navigation.findNavController(getView()).navigate(R.id.action_daySchedulesFragment_to_mainFragment);
                return;
            };

            ArrayList<String> entries = schedule.getEntryTimes();
            ArrayList<String> departures = schedule.getDepartureTimes();

            dayEntryScheduleAdapter.setArguments(entries, TypeSchedule.ENTRY);
            dayDepartureScheduleAdapter.setArguments(departures, TypeSchedule.DEPARTURE);

            boolean hasEntries = entries != null && entries.size() > 0;
            fragmentDaySchedulesBinding.noEntriesResultsTextView.setVisibility(hasEntries ? View.GONE : View.VISIBLE);

            boolean hasDepartures = departures != null && departures.size() > 0;
            fragmentDaySchedulesBinding.noDeparturesResultsTextView.setVisibility(hasDepartures ? View.GONE : View.VISIBLE);
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
        recyclerView.setAdapter(dayDepartureScheduleAdapter);

        RecyclerTouchListener touchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {

                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.edit_image_view, R.id.delete_image_view)
                .setTypeSchedule(typeSchedule)
                .setSwipeable(R.id.container_schedule, R.id.container_actions, (viewID, position, parentId) -> {
                    switch (viewID) {
                        case R.id.delete_image_view:
                            if(parentId == fragmentDaySchedulesBinding.entriesRecyclerView.getId()){
                                if(daySchedulesViewModel.getCanAddEntry().getValue())
                                    createDialogNeedHaveEntry();
                                else
                                    daySchedulesViewModel.deleteSchedule(position, touchListener.getTypeSchedule());
                            }
                            else{
                                if(daySchedulesViewModel.getCanAddDeparture().getValue())
                                    createDialogNeedHaveDeparture();
                                else
                                    daySchedulesViewModel.deleteSchedule(position, touchListener.getTypeSchedule());
                            }
                            break;
                        case R.id.edit_image_view:
                            setTimePickerDialog(getView(), touchListener.getTypeSchedule(), position);
                            break;

                    }
                });

        recyclerView.addOnItemTouchListener(touchListener);
    }

    private void setFabButtons() {
        ViewAnimation.init(fragmentDaySchedulesBinding.entryFloatingActionButton, fragmentDaySchedulesBinding.entryFloatingTextView);
        ViewAnimation.init(fragmentDaySchedulesBinding.departureFloatingActionButton, fragmentDaySchedulesBinding.departureFloatingTextView);
        ViewAnimation.init(fragmentDaySchedulesBinding.removeFloatingActionButton, fragmentDaySchedulesBinding.removeFloatingTextView);

        fragmentDaySchedulesBinding.actionsFloatingActionButton.setOnClickListener(view -> {
            setAnimationActionsFabButton();
        });

        fragmentDaySchedulesBinding.removeFloatingActionButton.setOnClickListener(view -> {
            setAnimationActionsFabButton();
            createDialogRemoveSchedule();
        });

        fragmentDaySchedulesBinding.entryFloatingActionButton.setOnClickListener(view -> {
            setAnimationActionsFabButton();
            if(daySchedulesViewModel.getCanAddEntry().getValue())
                setTimePickerDialog(view, TypeSchedule.ENTRY, null);
            else
                createDialogNeedHaveDeparture();
        });

        fragmentDaySchedulesBinding.departureFloatingActionButton.setOnClickListener(view -> {
            setAnimationActionsFabButton();
            if(daySchedulesViewModel.getCanAddDeparture().getValue())
                setTimePickerDialog(view, TypeSchedule.DEPARTURE, null);
            else
                createDialogNeedHaveEntry();
        });
    }

    private void setTimePickerDialog(View view, final TypeSchedule typeSchedule, @Nullable Integer position) {
        LocalDateTime now = LocalDateTime.now();
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), (timePicker, hourOfDay, minute) -> {
            LocalDateTime newTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), hourOfDay, minute);
            if(position != null)
                daySchedulesViewModel.updateTime(position, typeSchedule, newTime);
            else if (typeSchedule == TypeSchedule.ENTRY)
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
            ViewAnimation.showIn(fragmentDaySchedulesBinding.removeFloatingActionButton, fragmentDaySchedulesBinding.removeFloatingTextView);
        } else {
            ViewAnimation.showOut(fragmentDaySchedulesBinding.entryFloatingActionButton, fragmentDaySchedulesBinding.entryFloatingTextView);
            ViewAnimation.showOut(fragmentDaySchedulesBinding.departureFloatingActionButton, fragmentDaySchedulesBinding.departureFloatingTextView);
            ViewAnimation.showOut(fragmentDaySchedulesBinding.removeFloatingActionButton, fragmentDaySchedulesBinding.removeFloatingTextView);
        }
    }

    private void createDialogRemoveSchedule(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle(R.string.remove_schedule);
        builder.setMessage(R.string.remove_schedule_ask);

        builder.setPositiveButton(R.string.yes, (dialog, id) -> {
            daySchedulesViewModel.deleteSchedule(daySchedulesViewModel.getSchedule().getValue());
        });

        builder.setNegativeButton(R.string.no, (dialog, id) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createDialogNeedHaveEntry(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle(R.string.departure);
        builder.setMessage(R.string.one_entry_more_mandatory);

        builder.setPositiveButton(R.string.OK, (dialog, id) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createDialogNeedHaveDeparture(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle(R.string.entry);
        builder.setMessage(R.string.one_departure_more_mandatory);

        builder.setPositiveButton(R.string.OK, (dialog, id) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
