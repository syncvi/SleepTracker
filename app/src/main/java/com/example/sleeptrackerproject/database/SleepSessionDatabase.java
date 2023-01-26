package com.example.sleeptrackerproject.database;

import android.content.Context;
import com.example.sleeptrackerproject.SleepSession;
import com.example.sleeptrackerproject.SleepSessionData;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {SleepSession.class, SleepSessionData.class}, version = 5, exportSchema = false)
public abstract class SleepSessionDatabase extends RoomDatabase {
    public abstract SleepSessionDao sleepSessionDao();

    private static SleepSessionDatabase _dbInstance;

    public static synchronized SleepSessionDatabase getInstance(Context context) {
        if (_dbInstance == null) {
            _dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            SleepSessionDatabase.class, "sleep_session_database")
                    /*had to change the version and destroy the previous one
                    it drops all the tables and rebuilds them */
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return _dbInstance;
    }
}

