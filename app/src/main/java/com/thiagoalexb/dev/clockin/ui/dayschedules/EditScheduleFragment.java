package com.thiagoalexb.dev.clockin.ui.dayschedules;


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
import com.thiagoalexb.dev.clockin.data.TypeSchedule;
import com.thiagoalexb.dev.clockin.databinding.FragmentEditScheduleBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleAdapter;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.inject.Inject;


public class EditScheduleFragment extends BaseFragment {

    @Inject
    public ViewModelProviderFactory modelProviderFactory;

    public FragmentEditScheduleBinding fragmentEditScheduleBinding;
    public EditScheduleViewModel editScheduleViewModel;
    private int position;
    private TypeSchedule typeSchedule;
    private int scheduleId;

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

        if(bundle == null) return;
    }

    private void setSubscribes(){
        editScheduleViewModel.getStatusInsert().removeObservers(getViewLifecycleOwner());
        editScheduleViewModel.getStatusInsert().observe(getViewLifecycleOwner(), status -> {
            if(status != null){
                Bundle bundle = new Bundle();
                bundle.putInt(ScheduleAdapter.ID_KEY, scheduleId);
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_editScheduleFragment_to_daySchedulesFragment, bundle);
            }

        });

        editScheduleViewModel.getScheduleResource().removeObservers(getViewLifecycleOwner());
        editScheduleViewModel.getScheduleResource().observe(getViewLifecycleOwner(), resource -> {
           setLoading(resource.status);
        });
    }

    private void setEvents(){
        fragmentEditScheduleBinding.scheduleTextView.setOnFocusChangeListener((view, hasFocus) ->{
            if(!hasFocus) return;
            setTimePickerDialog(view);
        });

        fragmentEditScheduleBinding.insertAddressButton.setOnClickListener(view ->{
            editScheduleViewModel.save(position, typeSchedule);
        });
    }

    private void setTimePickerDialog(View view){
        LocalDateTime now = LocalDateTime.now();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this::onScheduleTimeSet, now.getHour(), now.getMinute(), true);

        timePickerDialog.setOnDismissListener(dialog -> {
            fragmentEditScheduleBinding.scheduleTextView.clearFocus();
        });

        timePickerDialog.show();
    }

    private void onScheduleTimeSet(TimePicker view, int hourOfDay, int minute){
        String entryTime = hourOfDay + ":" + minute;
        fragmentEditScheduleBinding.scheduleTextView.setText(entryTime);
        fragmentEditScheduleBinding.scheduleTextView.clearFocus();
    }

}
