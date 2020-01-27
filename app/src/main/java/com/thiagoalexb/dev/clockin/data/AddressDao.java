package com.thiagoalexb.dev.clockin.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Address address);

    @Update
    Completable update(Address address);

    @Query("SELECT * FROM Address LIMIT 1")
    Flowable<Address> get();
}
