package com.example.sleeptrackerproject;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Room;

@Entity(tableName = "sleep_session_data",
        foreignKeys = @ForeignKey(entity = SleepSession.class,
                parentColumns = "id",
                childColumns = "session_id",
                onDelete = CASCADE))
public class SleepSessionData {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public float rating;
    public String text;

    @ColumnInfo(name = "session_id")
    public long sessionId;

    public SleepSessionData(long sessionId, float rating, String text) {
        this.sessionId = sessionId;
        this.rating = rating;
        this.text = text;
    }
}

