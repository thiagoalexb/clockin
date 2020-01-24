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

public class MainFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MainFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private GoogleApiClient mClient;
    private Geofencing mGeofencing;
    private FragmentMainBinding fragmentMainBinding;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        askLocationPermission();

        setGoogleClient();

        setElements();

        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mClient.stopAutoManage(getActivity());
//        mClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "API Client Connection Successful!");

        setGeofencing();

        AppDatabase db = AppDatabase.getInstance(getContext());
        Address address = db.addressDao().get();

        if(address != null) {
            mGeofencing.updateGeofencesList(address);
            mGeofencing.registerAllGeofences();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "API Client Connection Suspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "API Client Connection Failed!");
    }

    public void askLocationPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    private void setGoogleClient(){
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
        fragmentMainBinding.placeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_addressFragment);
            }
        });
    }

}
