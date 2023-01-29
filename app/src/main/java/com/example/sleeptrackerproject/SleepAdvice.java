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
            ((TextView) findViewById(R.id.advice_text_title_view)).setText(R.string.not_enough_sleep_title);
            ((TextView) findViewById(R.id.advice_text_view)).setText(R.string.not_enough_sleep);
        }
        else if(averageDuration>5){
            ((TextView) findViewById(R.id.advice_text_title_view)).setText(R.string.too_much_sleep_title);
            ((TextView) findViewById(R.id.advice_text_view)).setText(R.string.too_much_sleep);
        }
        else{
            ((TextView) findViewById(R.id.advice_text_title_view)).setText(R.string.good_sleep);
            ((TextView) findViewById(R.id.advice_text_view)).setText("");
        }
    }
}