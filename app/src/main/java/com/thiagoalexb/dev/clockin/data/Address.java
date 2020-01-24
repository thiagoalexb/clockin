package com.thiagoalexb.dev.clockin.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Address {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public String state;
    public String city;
    public String neighborhood;
    public String street;
    public Integer number;
    public double latitude;
    public double longitude;
}
