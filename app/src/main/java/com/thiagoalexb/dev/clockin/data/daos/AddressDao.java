package com.thiagoalexb.dev.clockin.data.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.thiagoalexb.dev.clockin.data.models.Address;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Address address);

    @Update
    Completable update(Address address);

    @Query("SELECT * FROM Address LIMIT 1")
    Maybe<Address> get();
}
