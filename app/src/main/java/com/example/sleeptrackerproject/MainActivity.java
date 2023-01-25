package com.example.sleeptrackerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.app.PendingIntent;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.example.sleeptrackerproject.database.SleepSessionDao;
import com.example.sleeptrackerproject.database.SleepSessionDatabase;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat _notificationManager;
    private Button _startStopButton;
    private boolean _isTracking = false;
    private long _startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = preferences.getBoolean("isNightMode", false);
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //-----------------------WEATHER ACTIVITY SWITCHER-----------------------
        Button weatherButton = (Button) findViewById(R.id.weather_button);
        weatherButton.setOnClickListener(new View.OnClickListener() { //change to lambda later
            @Override
            public void onClick(View view) {
                // weather activity is activated when clicked
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });

        //-----------------------DARK MODE ENABLER-----------------------
        Button toggleButton = (Button) findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(view -> toggleNightMode());

        //-----------------------NOTIFICATION CHANNEL AND MANAGER-----------------------
        createNotificationChannel();
        _notificationManager = NotificationManagerCompat.from(this);




        //-----------------------SLEEP SESSIONS TRACKING-----------------------
        _startStopButton = findViewById(R.id.start_stop_button);
        _startStopButton.setOnClickListener(v -> {
            if (_isTracking) {
                stopTracking();
            } else {
                startTracking();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.sleep_session_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SleepSessionDao sleepSessionDao = SleepSessionDatabase.getInstance(this).sleepSessionDao();
        LiveData<List<SleepSession>> sleepSessionsLiveData = sleepSessionDao.getAllSleepSessions();
        SleepSessionAdapter adapter = new SleepSessionAdapter(sleepSessionsLiveData);
        recyclerView.setAdapter(adapter);
        sleepSessionsLiveData.observe(this, sleepSessions -> adapter.notifyDataSetChanged()); //sonarLint says that we shouldn't use it but w/e

        //-----------------------CLEAR ALL SLEEP SESSIONS-----------------------
        Button deleteAllButton = findViewById(R.id.delete_all_button);
        deleteAllButton.setOnClickListener(v -> new Thread(this::deleteAllEntries).start());
    }

    private void startTracking() {
        _startTime = System.currentTimeMillis();
        _isTracking = true;
        _startStopButton.setText("Stop");
    }

    private void stopTracking() {
        long endTime = System.currentTimeMillis();
        long duration = endTime - _startTime;
        _isTracking = false;
        _startStopButton.setText("Start"); //change it to android resources later

        new Thread(() -> createSleepSessionEntry(_startTime, endTime, duration)).start();

    }

    private void createSleepSessionEntry(long startTime, long endTime, long duration) {
        // make a new sleepsession and put it in the database
        SleepSession session = new SleepSession(startTime, endTime, duration);
        SleepSessionDatabase.getInstance(this).sleepSessionDao().insert(session);
    }

    private void deleteAllEntries() {
        // wipe all the sleep sessions
        SleepSessionDao sleepSessionDao = SleepSessionDatabase.getInstance(this).sleepSessionDao();
        sleepSessionDao.deleteAll();
    }


    private void createNotificationChannel() {
        /* create the NotificationChannel, but only on API 26+ because
        the NotificationChannel class is new and not in the support library */
        // everything from android docuXDD
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("my_channel_id", name, importance);
            channel.setDescription(description);
            // register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    public void sendNotification(String title, String message) {
        // create an explicit intent for an Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(123, builder.build());
    }



    public void checkSensors(View v) {
        Intent intent = new Intent(this, SensorActivity.class);
        startActivity(intent);
        sendNotification("Environment", "Sensor check commencing...");
    }

    public void editDetails(View v){
        Intent i = new Intent(this, TestActivity.class);
        String sessionName = (String) ((Button)v).getText();
        i.putExtra("NAME", sessionName);
        startActivity(i);
    }

    protected void toggleNightMode() {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            // light mode on
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            saveNightModeState(false);
        } else {
            // dark mode on
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            saveNightModeState(true);
        }

        // apply
        recreate();
    }

    private void saveNightModeState(boolean isNightMode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isNightMode", isNightMode);
        editor.apply();
    }
}