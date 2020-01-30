package com.thiagoalexb.dev.clockin.data;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Schedule {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public LocalDate date;
    public LocalTime entryTime;
    public LocalTime departureTime;
    public int day;
    public int month;
    public int year;
}
