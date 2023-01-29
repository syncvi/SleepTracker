package com.example.sleeptrackerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;

public class AlarmClockActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button setAlarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        timePicker = findViewById(R.id.time_picker);
        setAlarmButton = findViewById(R.id.set_alarm_button);

        // set the initial value for the time picker
        Calendar calendar = Calendar.getInstance();
        timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(calendar.get(Calendar.MINUTE));

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                setAlarm(hour, minute);
            }
        });

        if (getIntent().getBooleanExtra("show_dialog", false)) {
            showAlarmDialog();
        }
    }

    private void setAlarm(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // if the desired hour has already passed today, set the alarm for the next day
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(AlarmClockActivity.this, AlarmReceiver.class);
        // if a pending intent of a request code already exists, keep it but update it's contetns
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmClockActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Toast.makeText(AlarmClockActivity.this, "Alarm set for " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    public void showAlarmDialog() {
        DialogFragment dialog = new DialogFragment();
        dialog.show(getSupportFragmentManager(), "DialogFragment");
    }

}