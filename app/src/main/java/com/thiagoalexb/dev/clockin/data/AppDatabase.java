package com.thiagoalexb.dev.clockin.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.thiagoalexb.dev.clockin.data.converters.Converters;
import com.thiagoalexb.dev.clockin.data.daos.AddressDao;
import com.thiagoalexb.dev.clockin.data.daos.ScheduleDao;
import com.thiagoalexb.dev.clockin.data.models.Address;
import com.thiagoalexb.dev.clockin.data.models.Schedule;

@Database(entities = {
        Address.class,
        Schedule.class
}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "clockInDatabase";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract AddressDao addressDao();
    public abstract ScheduleDao scheduleDao();
}
