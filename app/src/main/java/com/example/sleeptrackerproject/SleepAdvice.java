package com.example.sleeptrackerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SleepAdvice extends AppCompatActivity {

    private float averageDuration;
    private long _totalSecs;
    private float _totalHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_advice);

        Intent i = getIntent();
        averageDuration = i.getFloatExtra("SLEEP_AVERAGE", 0);
        _totalSecs = (long)averageDuration;
        _totalHours = _totalSecs/3600;

        System.err.println(averageDuration);
        System.err.println(_totalSecs);
        System.err.println(_totalHours);
        if(averageDuration<1){
            ((TextView) findViewById(R.id.advice_text_view)).setText("You are not sleeping enough! Take care of yourself.");
        }
        else if(averageDuration>5){
            ((TextView) findViewById(R.id.advice_text_view)).setText("You are sleeping too much, it's not healthy!");
        }
        else{
            ((TextView) findViewById(R.id.advice_text_view)).setText("You are sleeping well enough.");
        }
    }
}