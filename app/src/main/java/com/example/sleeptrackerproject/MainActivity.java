package com.example.sleeptrackerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.sleeptrackerproject.database.SleepSessionDao;
import com.example.sleeptrackerproject.database.SleepSessionDatabase;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat _notificationManager;
    private Button _startStopButton;
    private boolean _isTracking = false;
    private long _startTime;
    private int _sessionNumber = 0;
    private Chronometer _chronometer;
    private boolean _isRunning;
    private int _itemCount = 0;
    private float averageDuration;

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
        _chronometer = findViewById(R.id.chronometer);
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


        sleepSessionsLiveData.observe(this, sleepSessions -> {
            updateAverageSleepTime();
            adapter.notifyDataSetChanged();
            _itemCount = sleepSessions.size();
            System.err.println(_itemCount);
            TextView sessionCountTextView = findViewById(R.id.session_count_text_view);
            sessionCountTextView.setText(String.valueOf(_itemCount));
        });




        //-----------------------CLEAR ALL SLEEP SESSIONS-----------------------
        Button deleteAllButton = findViewById(R.id.delete_all_button);
        deleteAllButton.setOnClickListener(v ->
        {
            new Thread(this::deleteAllEntries).start();
            TextView averageSleepTimeTextView = findViewById(R.id.average_sleep_time_text_view);
            averageSleepTimeTextView.setText("0");

        });

    }

    private void startTracking() {
        _chronometer.setBase(SystemClock.elapsedRealtime());
        _chronometer.start();
        _isRunning = true;
        _chronometer.setVisibility(View.VISIBLE);
        _startTime = System.currentTimeMillis() /1000;
        _isTracking = true;
        _startStopButton.setText("Stop");
    }

    private void stopTracking() {
        _chronometer.stop();
        _isRunning = false;
        _chronometer.setVisibility(View.GONE);
        long endTime = System.currentTimeMillis()/1000;
        long duration = endTime - _startTime;
        _isTracking = false;
        _startStopButton.setText("Start"); //change it to android resources later

        new Thread(() -> createSleepSessionEntry(_startTime, endTime, duration)).start();

    }

    @SuppressLint("DefaultLocale")
    public void updateAverageSleepTime() {
        SleepSessionDao sleepSessionDao = SleepSessionDatabase.getInstance(this).sleepSessionDao();
        LiveData<List<SleepSession>> sleepSessionsLiveData = sleepSessionDao.getAllSleepSessions();
        sleepSessionsLiveData.observe(this, sleepSessions -> {
            if (sleepSessions != null && !sleepSessions.isEmpty()) {
                long totalDuration = 0;
                for (SleepSession sleepSession : sleepSessions) {
                    totalDuration += sleepSession.getDuration();
                }
                averageDuration = totalDuration / sleepSessions.size();
                TextView averageSleepTimeTextView = findViewById(R.id.average_sleep_time_text_view);
                averageSleepTimeTextView.setText(String.format("%.2f hours", averageDuration / 3600.0));
            }
        });
    }


    private void createSleepSessionEntry(long startTime, long endTime, long duration) {
        // make a new sleepsession and put it in the database
        SleepSession session = new SleepSession(startTime, endTime, duration);
        SleepSessionDatabase.getInstance(this).sleepSessionDao().insert(session);
        SleepSessionData sessionData = new SleepSessionData(session.getId(), 0, "");
        //SleepSessionDatabase.getInstance(this).sleepSessionDao().insertData(sessionData);
        //ID is 0 every time? but sessionNumber increments itself by o=o+1, nice
        //autoGenerate=true doesn't work, clueless, will tackle it later
        System.err.println(session.toString());
        System.err.println(sessionData.toString());
        _sessionNumber++;
    }

    private void deleteAllEntries() {
        // wipe all the sleep sessions
        SleepSessionDao sleepSessionDao = SleepSessionDatabase.getInstance(this).sleepSessionDao();
        sleepSessionDao.deleteAll();
        _sessionNumber=0;
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


    public void checkSensors(View v) {
        Intent intent = new Intent(this, SensorActivity.class);
        startActivity(intent);
        NotificationHelper.sendNotification(this,"Environment", "Sensor check commencing...");
    }

    public void editDetails(View v){
        Intent i = new Intent(this, TestActivity.class);
        i.putExtra("SESSION_NUMBER", _sessionNumber);
        startActivity(i);
    }

    public void sleepAdvice(View v){
        Intent i = new Intent(this, SleepAdvice.class);
        i.putExtra("SLEEP_AVERAGE", averageDuration);
        startActivity(i);
    }
    public void setAlaram(View v){
        Intent i = new Intent(this, AlarmClockActivity.class);
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