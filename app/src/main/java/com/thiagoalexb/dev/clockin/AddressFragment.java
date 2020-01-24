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

import com.thiagoalexb.dev.clockin.data.AppDatabase;

import java.io.IOException;
import java.util.List;

public class AddressFragment extends Fragment {

    ViewDataBinding view;
    EditText state;
    EditText city;
    EditText neighborhood;
    EditText street;
    EditText number;

    public AddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false);

        state = view.getRoot().findViewById(R.id.state_edit_text);
        city = view.getRoot().findViewById(R.id.city_edit_text);
        neighborhood = view.getRoot().findViewById(R.id.neighborhood_edit_text);
        street = view.getRoot().findViewById(R.id.street_edit_text);
        number = view.getRoot().findViewById(R.id.number_edit_text);

        state.setText("PR");
        city.setText("Curitiba");
        neighborhood.setText("Guabirotuba");
        street.setText("Rua Ministro Gabriel Passos");
        number.setText("360");


        Button button = view.getRoot().findViewById(R.id.save_address_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = street.getText() + ", " + number.getText() + ", " + neighborhood.getText() + " - " + city.getText() + ", " + state.getText();
                getLocationFromAddress(address);
                Navigation.findNavController(v).navigate(R.id.action_addressFragment_to_mainFragment);
            }
        });

        return view.getRoot();
    }

    public void getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,1);

            if (address == null) {
                return;
            }

            Address location = address.get(0);

            AppDatabase db = AppDatabase.getInstance(getContext());

            com.thiagoalexb.dev.clockin.data.Address addressDatabase = new com.thiagoalexb.dev.clockin.data.Address();

            addressDatabase.state = state.getText().toString();
            addressDatabase.city = city.getText().toString();
            addressDatabase.neighborhood = neighborhood.getText().toString();
            addressDatabase.street = street.getText().toString();
            addressDatabase.number = Integer.parseInt(number.getText().toString());
            addressDatabase.latitude = location.getLatitude();
            addressDatabase.longitude = location.getLongitude();

            db.addressDao().insert(addressDatabase);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
