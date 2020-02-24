package com.thiagoalexb.dev.clockin.data.models;

import java.time.LocalDateTime;

public class SchedulePrint {

    public SchedulePrint(String date, String entryTime, String departureTime) {
        this.date = date;
        this.entryTime = entryTime;
        this.departureTime = departureTime;
    }

    private String date;
    private String entryTime;
    private String departureTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}
