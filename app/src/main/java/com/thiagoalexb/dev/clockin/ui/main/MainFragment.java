package com.thiagoalexb.dev.clockin.ui.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelProviderFactory;
import com.thiagoalexb.dev.clockin.geofence.Geofencing;
import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.AppDatabase;
import com.thiagoalexb.dev.clockin.databinding.FragmentMainBinding;
import com.thiagoalexb.dev.clockin.ui.address.AddressViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainFragment extends DaggerFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @Inject
    ViewModelProviderFactory modelProviderFactory;
    MainViewModel mainViewModel;
    private static final String TAG = MainFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private GoogleApiClient mClient;
    private Geofencing mGeofencing;
    private FragmentMainBinding fragmentMainBinding;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        mainViewModel = ViewModelProviders.of(this, modelProviderFactory).get(MainViewModel.class);

        mainViewModel.checkAddress();

        mainViewModel.getAddress().observe(getViewLifecycleOwner(), address -> {
            Toast.makeText(getContext(), "Kkkkkkkkkkkkkk FDP", Toast.LENGTH_LONG);
        });

        getLocationPermission();

        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        setElements();
        setGoogleClient();
    }

    @Override
    public void onStart() {
        super.onStart();
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            setGoogleClient();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mDisposable.clear();
        if(mClient != null  && mClient.isConnected()) {
            mClient.stopAutoManage(getActivity());
            mClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Conectado no google client");
        setGeofencing();



        AppDatabase db = AppDatabase.getInstance(getContext());
        mDisposable.add(db.addressDao().get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(address -> {
                    mGeofencing.updateGeofencesList(address);
                    mGeofencing.registerAllGeofences();
                }, throwable -> {
                    navigateToAddress(getView());
                }));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Suspenso no google client");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Falha no google client");
    }

    private void getLocationPermission() {
        requestPermissions(new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    private void setGoogleClient() {
        if(mClient == null)
            mClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(getActivity(), this)
                    .build();
    }

    private void setGeofencing() {
        mGeofencing = new Geofencing(getContext(), mClient);
    }

    private void setElements() {
        fragmentMainBinding.placeBtn.setOnClickListener(view -> navigateToAddress(view));

        AppDatabase db = AppDatabase.getInstance(getContext());
        db.scheduleDao().getByMonth().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedules -> {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    fragmentMainBinding.schedulesRecyclerView.setLayoutManager(layoutManager);

                    ScheduleAdapter mAdapter = new ScheduleAdapter(schedules);
                    fragmentMainBinding.schedulesRecyclerView.setAdapter(mAdapter);


                    fragmentMainBinding.schedulesRecyclerView.addItemDecoration(
                            new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
                }, throwable -> {
                    Log.i(TAG, "ERRooooWW");
                });
    }

    private void navigateToAddress(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addressFragment);
    }

    private void createDialogAddressNotification(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.address);
        builder.setMessage(R.string.address_mandatory);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                navigateToAddress(getView());
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
