package com.example.sleeptrackerproject.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sleeptrackerproject.SleepSession;
import com.example.sleeptrackerproject.SleepSessionData;

import java.util.List;

@Dao
public interface SleepSessionDao {
    @Insert
    void insert(SleepSession sleepSession);

    @Update
    void update(SleepSession sleepSession);

    @Delete
    void delete(SleepSession sleepSession);

    @Query("SELECT * FROM sleep_session")
    LiveData<List<SleepSession>> getAllSleepSessions();

    @Query("SELECT * FROM sleep_session WHERE id = :id")
    LiveData<SleepSession> getSleepSessionById(long id);

    @Query("DELETE FROM sleep_session")
    void deleteAll();

    @Insert
    void insertData(SleepSessionData sleepSessionData);

    @Update
    void updateData(SleepSessionData sleepSessionData);

    @Delete
    void deleteData(SleepSessionData sleepSessionData);

    @Query("SELECT * FROM sleep_session_data WHERE session_id = :sessionId")
    SleepSessionData getSessionData(long sessionId);


}
