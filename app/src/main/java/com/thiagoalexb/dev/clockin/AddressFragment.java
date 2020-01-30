package com.thiagoalexb.dev.clockin;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thiagoalexb.dev.clockin.data.AppDatabase;
import com.thiagoalexb.dev.clockin.databinding.FragmentAddressBinding;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddressFragment extends Fragment {

    FragmentAddressBinding fragmentAddressBinding;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private Integer addressId = 0;
    private String addressUUID;

    public AddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentAddressBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false);

        getAddress();

        setElements();

        return fragmentAddressBinding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    private void getAddress() {
        AppDatabase db = AppDatabase.getInstance(getContext());

        mDisposable.add(db.addressDao().get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(address -> {
                    fragmentAddressBinding.stateEditText.setText(address.state);
                    fragmentAddressBinding.cityEditText.setText(address.city);
                    fragmentAddressBinding.neighborhoodEditText.setText(address.neighborhood);
                    fragmentAddressBinding.streetEditText.setText(address.street);
                    fragmentAddressBinding.numberEditText.setText(address.number.toString());
                    addressId = address.id;
                    addressUUID = address.addressUUID;
                }, throwable -> {

                }));
    }


    private void getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 1);

            if (address == null) {
                Toast.makeText(getContext(), "Endereço não encontrado", Toast.LENGTH_LONG);
                return;
            }

            Address location = address.get(0);

            AppDatabase db = AppDatabase.getInstance(getContext());

            com.thiagoalexb.dev.clockin.data.Address addressDatabase = new com.thiagoalexb.dev.clockin.data.Address();


            addressDatabase.state = fragmentAddressBinding.stateEditText.getText().toString();
            addressDatabase.city = fragmentAddressBinding.cityEditText.getText().toString();
            addressDatabase.neighborhood = fragmentAddressBinding.neighborhoodEditText.getText().toString();
            addressDatabase.street = fragmentAddressBinding.streetEditText.getText().toString();
            addressDatabase.number = Integer.parseInt(fragmentAddressBinding.numberEditText.getText().toString());
            addressDatabase.latitude = location.getLatitude();
            addressDatabase.longitude = location.getLongitude();
            if(addressId > 0) {
                addressDatabase.id = addressId;
                addressDatabase.addressUUID = addressUUID;
            }else
                addressDatabase.addressUUID = UUID.randomUUID().toString();


            mDisposable.add(db.addressDao().insert(addressDatabase).
                    observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(() -> {
                        Navigation.findNavController(getView()).navigate(R.id.action_addressFragment_to_mainFragment);
                    }, throwable -> {
                        Toast.makeText(getContext(), "Erro ao adicionar endereço", Toast.LENGTH_LONG);
                    }));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setElements() {
        Button button = fragmentAddressBinding.getRoot().findViewById(R.id.save_address_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = fragmentAddressBinding.streetEditText.getText()
                        + ", " + fragmentAddressBinding.numberEditText.getText()
                        + ", " + fragmentAddressBinding.neighborhoodEditText.getText()
                        + " - " + fragmentAddressBinding.cityEditText.getText()
                        + ", " + fragmentAddressBinding.stateEditText.getText();

                getLocationFromAddress(address);
            }
        });
    }

}
