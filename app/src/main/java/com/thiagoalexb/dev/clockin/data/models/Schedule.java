package com.thiagoalexb.dev.clockin.data.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Schedule {

    public Schedule(LocalDateTime date, LocalDateTime entryTime, int day, int month, int year) {
        this.date = date;
        this.entryTime = entryTime;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private LocalDateTime date;
    private LocalDateTime entryTime;
    private LocalDateTime departureTime;
    private int day;
    private int month;
    private int year;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
