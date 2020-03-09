package com.thiagoalexb.dev.clockin.ui.schedule;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.databinding.FragmentScheduleBinding;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.ui.animation.ViewAnimation;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;


public class ScheduleFragment extends BaseFragment {

    private static final int PERMISSIONS_REQUEST = 177;

    @Inject
    public ViewModelProviderFactory modelProviderFactory;
    @Inject
    public ScheduleAdapter scheduleAdapter;

    private ScheduleViewModel scheduleViewModel;
    private FragmentScheduleBinding fragmentScheduleBinding;
    private Boolean isRotate = false;

    public ScheduleFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        scheduleViewModel = ViewModelProviders.of(this, modelProviderFactory).get(ScheduleViewModel.class);

        fragmentScheduleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false);

        fragmentScheduleBinding.setScheduleViewModel(scheduleViewModel);

        fragmentScheduleBinding.setLifecycleOwner(this);

        getLocationPermission();

        setObservers();

        setElements();

        return fragmentScheduleBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasLocationPermission())
            scheduleViewModel.checkAddress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(hasLocationPermission())
            scheduleViewModel.checkAddress();
        else
            createDialogLocationNotification();
    }

    private void getLocationPermission() {
        if (hasLocationPermission()) return;

        requestPermissions(getPermissionsToAsk(), PERMISSIONS_REQUEST);
    }

    private String[] getPermissionsToAsk(){
        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.Q)
            return new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION };
        else
            return new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
    }

    private boolean hasLocationPermission(){
        int permissionLocationFine = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionLocationCoarse = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            return permissionLocationFine == PackageManager.PERMISSION_GRANTED && permissionLocationCoarse == PackageManager.PERMISSION_GRANTED;

        int permissionBackgroundLocation = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        return permissionLocationFine == PackageManager.PERMISSION_GRANTED && permissionLocationCoarse == PackageManager.PERMISSION_GRANTED && permissionBackgroundLocation == PackageManager.PERMISSION_GRANTED;


    }

    private void setObservers(){

        scheduleViewModel.getAddress().removeObservers(getViewLifecycleOwner());
        scheduleViewModel.getAddress().observe(getViewLifecycleOwner(), this::validateAddress);

        scheduleViewModel.getSchedules().removeObservers(getViewLifecycleOwner());
        scheduleViewModel.getSchedules().observe(getViewLifecycleOwner(), resource -> {
            setLoading(resource.status);
            validateSchedules(resource.data);
        });
    }

    private void setElements() {
        scheduleAdapter.setSchedules(new ArrayList<>());
        fragmentScheduleBinding.schedulesRecyclerView.setAdapter(scheduleAdapter);

        ViewAnimation.init(fragmentScheduleBinding.reportFloatingActionButton, fragmentScheduleBinding.reportTextView);
        ViewAnimation.init(fragmentScheduleBinding.addressFloatingActionButton, fragmentScheduleBinding.addressTextView);
        ViewAnimation.init(fragmentScheduleBinding.addDayFloatingActionButton, fragmentScheduleBinding.addDayTextView);

        fragmentScheduleBinding.actionsFloatingActionButton.setOnClickListener(view -> {
            setAnimationActionsFabButton();
        });

        fragmentScheduleBinding.addressFloatingActionButton.setOnClickListener(this::navigateToAddress);
        fragmentScheduleBinding.reportFloatingActionButton.setOnClickListener(this::navigateToReport);
        fragmentScheduleBinding.addDayFloatingActionButton.setOnClickListener(view -> {
            setAnimationActionsFabButton();
            scheduleViewModel.addDay();
        });
    }

    private void setAnimationActionsFabButton(){
        isRotate = ViewAnimation.rotateFab(fragmentScheduleBinding.actionsFloatingActionButton, !isRotate);

        if(isRotate){
            ViewAnimation.showIn(fragmentScheduleBinding.reportFloatingActionButton, fragmentScheduleBinding.reportTextView);
            ViewAnimation.showIn(fragmentScheduleBinding.addressFloatingActionButton, fragmentScheduleBinding.addressTextView);
            ViewAnimation.showIn(fragmentScheduleBinding.addDayFloatingActionButton, fragmentScheduleBinding.addDayTextView);
        }else{
            ViewAnimation.showOut(fragmentScheduleBinding.reportFloatingActionButton, fragmentScheduleBinding.reportTextView);
            ViewAnimation.showOut(fragmentScheduleBinding.addressFloatingActionButton, fragmentScheduleBinding.addressTextView);
            ViewAnimation.showOut(fragmentScheduleBinding.addDayFloatingActionButton, fragmentScheduleBinding.addDayTextView);
        }
    }

    private void navigateToAddress(View view) {
        setAnimationActionsFabButton();
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addressFragment);
    }

    private void navigateToReport(View view) {
        setAnimationActionsFabButton();
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_reportFragment);
    }

    private void createDialogLocationNotification(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle(R.string.location);
        builder.setMessage(R.string.location_permission_mandatory);

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getLocationPermission();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void validateAddress(Address address){
        if(address == null)
            navigateToAddress(getView());
        else
            scheduleViewModel.checkSchedules();
    }

    private void validateSchedules(List<Schedule> schedules){
        if(schedules == null) return;

        scheduleAdapter.setSchedules(schedules);
    }
}
