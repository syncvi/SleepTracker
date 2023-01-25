package com.example.sleeptrackerproject;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

    public void bind(SleepSession sleepSession) {
        _startTimeTextView.setText("Start Time: " + sleepSession.getStartTime());
        _endTimeTextView.setText("End Time: " + sleepSession.getEndTime());
        _durationTimeTextView.setText("Duration: " + sleepSession.getDuration());
    }
}
