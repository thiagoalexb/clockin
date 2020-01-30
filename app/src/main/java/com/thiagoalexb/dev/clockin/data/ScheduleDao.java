package com.thiagoalexb.dev.clockin.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface  ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Schedule schedule);

    @Update
    Completable update(Schedule schedule);

    @Query("SELECT * FROM Schedule WHERE year = :year AND month = :month AND day = :day LIMIT 1")
    Single<Schedule> getByDay(int year, int month, int day);

    @Query("SELECT * FROM Schedule")
    Flowable<List<Schedule>> getByMonth();
}
