package com.thiagoalexb.dev.clockin.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AddressDao {

    @Insert
    void insert(Address address);

    @Update
    void update(Address address);

    @Query("SELECT * FROM Address LIMIT 1")
    Address get();
}
