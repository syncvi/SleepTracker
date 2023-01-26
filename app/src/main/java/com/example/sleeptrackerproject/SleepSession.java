package com.example.sleeptrackerproject;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
//maybe we will change it to lombok later idk
@Entity(tableName = "sleep_session")
public class SleepSession {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "start_time")
    private long startTime;
    @ColumnInfo(name = "end_time")
    private long endTime;
    @ColumnInfo(name = "duration")
    private long duration;
    @ColumnInfo(name = "session_number")
    private int sessionNumber;
    private static int currentSessionNumber = 0;

    public SleepSession(long startTime, long endTime, long duration) {
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionNumber = ++currentSessionNumber;
    }

    public int getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(int sessionNumber) {
        this.sessionNumber=sessionNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @NonNull
    @Override
    public String toString() {
        return "SleepSession{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime='" + endTime + '\'' +
                ", duration=" + duration +
                ", sessionNumber=" + sessionNumber +
                '}';
    }
}
