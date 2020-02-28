package com.thiagoalexb.dev.clockin.ui.schedule;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private static final int PERMISSIONS_REQUEST_LOCATION_CODE = 177;

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

        setHasOptionsMenu(true);

        return fragmentScheduleBinding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(hasLocationPermission())
            scheduleViewModel.checkAddress();
        else
            createDialogLocationNotification();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasLocationPermission())
            scheduleViewModel.checkAddress();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.address_menu_item:
                navigateToAddress(getView());
                return true;
            case R.id.reports_menu_item:
                navigateToReport(getView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getLocationPermission() {
        if (!hasLocationPermission())
            requestPermissions(new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, PERMISSIONS_REQUEST_LOCATION_CODE);
    }

    private boolean hasLocationPermission(){
        int permissionLocationFine = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionLocationCoarse = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionLocationFine == PackageManager.PERMISSION_GRANTED && permissionLocationCoarse == PackageManager.PERMISSION_GRANTED;
    }

    private void setElements() {
        scheduleAdapter.setSchedules(new ArrayList<>());
        fragmentScheduleBinding.schedulesRecyclerView.setAdapter(scheduleAdapter);
        fragmentScheduleBinding.addDayFloatingActionButton.setOnClickListener(view -> {
            isRotate = ViewAnimation.rotateFab(view, !isRotate);
//            scheduleViewModel.addDay();
            //https://medium.com/better-programming/animated-fab-button-with-more-options-2dcf7118fff6
        });
    }

    private void navigateToAddress(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addressFragment);
    }

    private void navigateToReport(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_reportFragment);
    }

    private void createDialogLocationNotification(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle(R.string.location);
        builder.setMessage(R.string.location_permission_mandatory);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getLocationPermission();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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

    private void validateAddress(Address address){
        if(address == null)
            navigateToAddress(getView());
        else
            scheduleViewModel.checkSchedules(LocalDateTime.now().getMonthValue());
    }

    private void validateSchedules(List<Schedule> schedules){
        if(schedules == null) return;

        scheduleAdapter.setSchedules(schedules);
    }
}