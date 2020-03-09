package com.thiagoalexb.dev.clockin.ui.editschedule;


import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.databinding.FragmentEditScheduleBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleAdapter;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class EditScheduleFragment extends BaseFragment {

    @Inject
    public ViewModelProviderFactory modelProviderFactory;

    public FragmentEditScheduleBinding fragmentEditScheduleBinding;
    public EditScheduleViewModel editScheduleViewModel;

    public EditScheduleFragment() {
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        editScheduleViewModel = ViewModelProviders.of(this, modelProviderFactory).get(EditScheduleViewModel.class);

        fragmentEditScheduleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_schedule, container, false);

        fragmentEditScheduleBinding.setEditScheduleViewModel(editScheduleViewModel);

        fragmentEditScheduleBinding.setLifecycleOwner(this);

        this.checkBundle();

        this.setSubscribes();

        this.setEvents();

        return fragmentEditScheduleBinding.getRoot();
    }

    private void checkBundle(){
        Bundle bundle = getArguments();

        if(bundle != null && bundle.containsKey(ScheduleAdapter.ID_KEY)){
            int scheduleId = bundle.getInt(ScheduleAdapter.ID_KEY);
            editScheduleViewModel.checkSchedule(scheduleId);
        }
    }

    private void setSubscribes(){
        editScheduleViewModel.getStatusInsert().removeObservers(getViewLifecycleOwner());
        editScheduleViewModel.getStatusInsert().observe(getViewLifecycleOwner(), status -> {
            if(status != null)
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_editScheduleFragment_to_mainFragment);
        });

        editScheduleViewModel.getScheduleResource().removeObservers(getViewLifecycleOwner());
        editScheduleViewModel.getScheduleResource().observe(getViewLifecycleOwner(), resource -> {
           setLoading(resource.status);
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

        timePickerDialog.setOnDismissListener(dialog -> {
            fragmentEditScheduleBinding.entryTextView.clearFocus();
            fragmentEditScheduleBinding.departureTextView.clearFocus();
        });

        timePickerDialog.show();
    }

    private void onEntryTimeSet(TimePicker view, int hourOfDay, int minute){
        String entryTime = hourOfDay + ":" + minute;
        fragmentEditScheduleBinding.entryTextView.setText(entryTime);
        fragmentEditScheduleBinding.entryTextView.clearFocus();
    }

    private void onDepartureTimeSet(TimePicker view, int hourOfDay, int minute){
        String departureTime = hourOfDay + ":" + minute;
        fragmentEditScheduleBinding.departureTextView.setText(departureTime);
        fragmentEditScheduleBinding.departureTextView.clearFocus();
    }

}
