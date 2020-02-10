package com.thiagoalexb.dev.clockin.data.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.thiagoalexb.dev.clockin.data.models.Schedule;

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

    @Query("SELECT * FROM Schedule WHERE year = :year AND month = :month AND day = :day LIMIT 1")
    Single<Schedule> getByDay(int year, int month, int day);

    @Query("SELECT * FROM Schedule")
    Flowable<List<Schedule>> getByMonth();
}
