package com.thiagoalexb.dev.clockin.ui.schedule;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.databinding.FragmentAddressBinding;
import com.thiagoalexb.dev.clockin.databinding.FragmentEditScheduleBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.ui.main.ScheduleAdapter;

import java.time.LocalDateTime;
import java.util.function.Function;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class EditScheduleFragment extends DaggerFragment {

    @Inject
    public ViewModelProviderFactory modelProviderFactory;

    public FragmentEditScheduleBinding fragmentEditScheduleBinding;
    public EditScheduleViewModel editScheduleViewModel;

    public EditScheduleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        editScheduleViewModel = ViewModelProviders.of(this, modelProviderFactory).get(EditScheduleViewModel.class);

        Bundle bundle = getArguments();

        if(bundle.containsKey(ScheduleAdapter.ID_KEY)){
            Integer scheduleId = bundle.getInt(ScheduleAdapter.ID_KEY);
            editScheduleViewModel.checkSchedule(scheduleId);
        }


        fragmentEditScheduleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_schedule, container, false);

        setSubscribes();

        setEvents();

        return fragmentEditScheduleBinding.getRoot();
    }

    private void setSubscribes(){
        editScheduleViewModel.getStatusInsert().removeObservers(getViewLifecycleOwner());
        editScheduleViewModel.getStatusInsert().observe(getViewLifecycleOwner(), status -> {
            if(status != null)
                Navigation.findNavController(getView()).navigate(R.id.action_editScheduleFragment_to_mainFragment);
        });
    }

    private void setEvents(){
        fragmentEditScheduleBinding.entryTextView.setOnFocusChangeListener((view, hasFocus) ->{
            if(!hasFocus) return;
            setTimePickerDialog(view);
        });

        fragmentEditScheduleBinding.departureTextView.setOnFocusChangeListener((view, hasFocus) ->{
            if(!hasFocus) return;
            setTimePickerDialog(view);
        });
    }

    private void setTimePickerDialog(View view){
        LocalDateTime now = LocalDateTime.now();
        TimePickerDialog timePickerDialog = null;
        if(view.getId() == fragmentEditScheduleBinding.entryTextView.getId())
            timePickerDialog = new TimePickerDialog(getContext(), this::onEntryTimeSet, now.getHour(), now.getMinute(), true);
        else
            timePickerDialog = new TimePickerDialog(getContext(), this::onDepartureTimeSet, now.getHour(), now.getMinute(), true);

        timePickerDialog.show();
    }

    public void onEntryTimeSet(TimePicker view, int hourOfDay, int minute){
        fragmentEditScheduleBinding.entryTextView.setText(hourOfDay + ":" + minute);
        fragmentEditScheduleBinding.entryTextView.clearFocus();
    }

    public void onDepartureTimeSet(TimePicker view, int hourOfDay, int minute){
        fragmentEditScheduleBinding.departureTextView.setText(hourOfDay + ":" + minute);
        fragmentEditScheduleBinding.departureTextView.clearFocus();
    }

}
