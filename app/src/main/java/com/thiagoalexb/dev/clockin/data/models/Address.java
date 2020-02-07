package com.thiagoalexb.dev.clockin.data.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Address extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String addressUUID;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private Integer number;
    private double latitude;
    private double longitude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddressUUID() {
        return addressUUID;
    }

    public void setAddressUUID(String addressUUID) {
        this.addressUUID = addressUUID;
    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        notifyPropertyChanged(BR.state);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
        notifyPropertyChanged(BR.neighborhood);
    }

    @Bindable
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
        notifyPropertyChanged(BR.street);
    }

    @Bindable
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
        notifyPropertyChanged(BR.number);
    }

    @Bindable
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Bindable
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
