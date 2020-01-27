package com.thiagoalexb.dev.clockin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.thiagoalexb.dev.clockin.data.Address;
import com.thiagoalexb.dev.clockin.data.AppDatabase;
import com.thiagoalexb.dev.clockin.databinding.FragmentMainBinding;

import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MainFragment.class.getSimpleName();
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

        askLocationPermission();

        setElements();

        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        setGoogleClient();
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
        Log.i(TAG, "Conectei kkk");
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

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void askLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    private void setGoogleClient() {
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
    }

    private void navigateToAddress(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addressFragment);
    }

}
