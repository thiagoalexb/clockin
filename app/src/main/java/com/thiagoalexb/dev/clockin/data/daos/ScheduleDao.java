package com.thiagoalexb.dev.clockin.data.daos;

import android.util.Pair;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.thiagoalexb.dev.clockin.data.models.Schedule;
import com.thiagoalexb.dev.clockin.data.models.ScheduleYearMonth;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface  ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(Schedule schedule);

    @Update
    Single<Integer> update(Schedule schedule);

    @Delete
    Single<Integer> delete (Schedule schedule);

    @Query("SELECT * FROM Schedule WHERE year = :year AND month = :month AND day = :day LIMIT 1")
    Single<Schedule> getByDay(int year, int month, int day);

    @Query("SELECT * FROM Schedule WHERE id = :id")
    Single<Schedule> getById(int id);

    @Query("SELECT * FROM Schedule WHERE month = :month")
    Flowable<List<Schedule>> getByMonth(int month);

    @Query("SELECT DISTINCT year, month FROM Schedule")
    Flowable<List<ScheduleYearMonth>> getYearsMonths();

    @Query("SELECT * FROM Schedule WHERE year = :year AND month = :month")
    Flowable<List<Schedule>> getByYearMonth(int year, int month);
}
