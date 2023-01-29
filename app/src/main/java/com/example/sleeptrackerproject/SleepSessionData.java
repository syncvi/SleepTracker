package com.example.sleeptrackerproject;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
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
    private long id;

    private float rating;
    private String text;

    @ColumnInfo(name = "session_id")
    private long sessionId;

    public SleepSessionData(long sessionId, float rating, String text) {
        this.sessionId = sessionId;
        this.rating = rating;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    @NonNull
    @Override
    public String toString() {
        //might change the format later
        return "SleepSessionData{" +
                "id=" + id +
                ", rating=" + rating +
                ", text='" + text + '\'' +
                ", sessionId=" + sessionId +
                '}';
    }
}

