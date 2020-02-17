package com.thiagoalexb.dev.clockin.ui.main;

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

import com.google.android.gms.common.api.GoogleApiClient;
import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.databinding.FragmentMainBinding;
import com.thiagoalexb.dev.clockin.ui.BaseFragment;
import com.thiagoalexb.dev.clockin.util.Resource;

import java.util.List;

import javax.inject.Inject;


public class MainFragment extends BaseFragment {

    private static final int PERMISSIONS_REQUEST_FINE_LOCATION_CODE = 111;

    @Inject
    public ViewModelProviderFactory modelProviderFactory;
    @Inject
    public ScheduleAdapter scheduleAdapter;
    @Inject
    public GoogleApiClient googleApiClient;

    private MainViewModel mainViewModel;
    private FragmentMainBinding fragmentMainBinding;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainViewModel = ViewModelProviders.of(this, modelProviderFactory).get(MainViewModel.class);

        fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        fragmentMainBinding.setMainViewModel(mainViewModel);

        fragmentMainBinding.setLifecycleOwner(this);

        getLocationPermission();

        setObservers();

        setElements();

        setHasOptionsMenu(true);

        setGoogleApiClient(googleApiClient);

        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSIONS_REQUEST_FINE_LOCATION_CODE && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            mainViewModel.checkAddress();
        else
            createDialogLocationNotification();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasLocationPermission())
            mainViewModel.checkAddress();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.address_menu_item:
                navigateToAddress(getView());
                return true;
            case R.id.reports_menu_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getLocationPermission() {
        if (!hasLocationPermission())
            requestPermissions(new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSIONS_REQUEST_FINE_LOCATION_CODE);
    }

    private boolean hasLocationPermission(){
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionLocation == PackageManager.PERMISSION_GRANTED;
    }

    private void setElements() {
        fragmentMainBinding.schedulesRecyclerView.setAdapter(scheduleAdapter);
    }

    private void navigateToAddress(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addressFragment);
    }

    private void createDialogLocationNotification(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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

        mainViewModel.getAddress().removeObservers(getViewLifecycleOwner());
        mainViewModel.getAddress().observe(getViewLifecycleOwner(), address -> {
            validateAddress(address);
        });

        mainViewModel.getSchedules().removeObservers(getViewLifecycleOwner());
        mainViewModel.getSchedules().observe(getViewLifecycleOwner(), resource -> {
            setLoading(resource.status == Resource.Status.LOADING);
            validateSchedules(resource.data);
        });
    }

    private void validateAddress(Address address){
        if(address == null)
            navigateToAddress(getView());
        else
            mainViewModel.checkSchedules();
    }

    private void validateSchedules(List<Schedule> schedules){
        if(schedules == null) return;

        scheduleAdapter.setSchedules(schedules);
    }
}
