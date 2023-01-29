package com.example.sleeptrackerproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.TimeUnit;


public class SleepSessionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView _startTimeTextView;
    private TextView _endTimeTextView;
    private TextView _durationTimeTextView;
    private TextView _numberTextView;
    private Context _anyContext;

    public SleepSessionViewHolder(View itemView, Context context) {
        super(itemView);
        _anyContext = context;
        _numberTextView = itemView.findViewById(R.id.number_text_view);
        _startTimeTextView = itemView.findViewById(R.id.sleep_session_start_time_text_view);
        _endTimeTextView = itemView.findViewById(R.id.sleep_session_end_time_text_view);
        _durationTimeTextView = itemView.findViewById(R.id.sleep_session_duration_time_text_view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void bind(SleepSession sleepSession) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_anyContext, TestActivity.class);
                int sessionNumber = Integer.parseInt(_numberTextView.getText().toString());
                intent.putExtra("SESSION_NUMBER", sessionNumber);
                _anyContext.startActivity(intent);
            }
        });


        long startTime = sleepSession.getStartTime();
        long endTime = sleepSession.getEndTime();
        long duration = sleepSession.getDuration();
        int orderNumber = sleepSession.getSessionNumber();
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
        _numberTextView.setText((String.valueOf(orderNumber)));
    }

    @Override
    public void onClick(View view) {

    }
}
