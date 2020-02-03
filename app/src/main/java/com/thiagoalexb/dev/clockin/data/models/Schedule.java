package com.thiagoalexb.dev.clockin.data.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Schedule {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public LocalDateTime date;
    public LocalDateTime entryTime;
    public LocalDateTime departureTime;
    public int day;
    public int month;
    public int year;
}
