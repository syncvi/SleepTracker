package com.example.sleeptrackerproject;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.TimeUnit;

public class SleepSessionViewHolder extends RecyclerView.ViewHolder {
    private TextView _startTimeTextView;
    private TextView _endTimeTextView;
    private TextView _durationTimeTextView;

    public SleepSessionViewHolder(View itemView) {
        super(itemView);
        _startTimeTextView = itemView.findViewById(R.id.sleep_session_start_time_text_view);
        _endTimeTextView = itemView.findViewById(R.id.sleep_session_end_time_text_view);
        _durationTimeTextView = itemView.findViewById(R.id.sleep_session_duration_time_text_view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void bind(SleepSession sleepSession) {
        long startTime = sleepSession.getStartTime();
        long endTime = sleepSession.getEndTime();
        long duration = sleepSession.getDuration();
        long hours = duration / 3600;
        long remainder = duration % 3600;
        long minutes = remainder / 60;
        long secs = remainder % 60;
        String output =  String.format("%02d:%02d:%02d", hours, minutes, secs);
        // String.format("%02d : %02d : %02d", TimeUnit.MILLISECONDS.toHours(endTime)%24, TimeUnit.MILLISECONDS.toMinutes(endTime)%60, TimeUnit.MILLISECONDS.toSeconds(endTime)%60));
        _startTimeTextView.setText("Start Time: " +
                String.format("%02d : %02d : %02d", (TimeUnit.MILLISECONDS.toHours(startTime)%24)+1, TimeUnit.MILLISECONDS.toMinutes(startTime)%60, TimeUnit.MILLISECONDS.toSeconds(startTime)%60));
        _endTimeTextView.setText("End Time: " +
                String.format("%02d : %02d : %02d", (TimeUnit.MILLISECONDS.toHours(endTime)%24)+1, TimeUnit.MILLISECONDS.toMinutes(endTime)%60, TimeUnit.MILLISECONDS.toSeconds(endTime)%60));
        _durationTimeTextView.setText("Duration: " + output);
    }
}
