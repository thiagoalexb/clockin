package com.thiagoalexb.dev.clockin.data.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.thiagoalexb.dev.clockin.util.DateHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Entity
public class Schedule {

    public Schedule(LocalDateTime date, ArrayList<String> entryTimes, int day, int month, int year) {
        this.date = date;
        this.entryTimes = entryTimes;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private LocalDateTime date;
    private ArrayList<String> entryTimes;
    private ArrayList<String> departureTimes;
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

    public ArrayList<String> getEntryTimes() {
        return entryTimes;
    }

    public void setEntryTimes(ArrayList<String> entryTimes) {
        this.entryTimes = entryTimes;
    }

    public ArrayList<String> getDepartureTimes() {
        return departureTimes;
    }

    public void setDepartureTimes(ArrayList<String> departureTimes) {
        this.departureTimes = departureTimes;
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

    public ArrayList<String> getEntryTimesFormatted(){
        ArrayList<String> formattedList = new ArrayList<>();
        for (String entry : this.entryTimes){
            formattedList.add(DateHelper.getHourMinute(entry));
        }
        return formattedList;
    }

    public ArrayList<String> getDepartureTimesFormatted(){
        ArrayList<String> formattedList = new ArrayList<>();
        for (String departure : this.departureTimes){
            formattedList.add(DateHelper.getHourMinute(departure));
        }
        return formattedList;
    }
}
