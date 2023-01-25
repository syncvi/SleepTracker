package com.example.sleeptrackerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager _sensorManager;
    private Sensor _lightSensor;
    private Sensor _humiditySensor;
    private Sensor _temperatureSensor;

    //MainActivity _mainActivity = ((MainActivity) getApplicationContext()).getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        _sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        _lightSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        _humiditySensor = _sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        _temperatureSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);


    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LIGHT){
            float illuminance = event.values[0];

            TextView illuminanceTextView = findViewById(R.id.illuminance_textview);
            TextView luxRecommendationTextView = findViewById(R.id.lux_recommendation_textview);
            illuminanceTextView.setText(String.valueOf(illuminance));

            if(Float.parseFloat((String.valueOf(illuminance)))>5){
                luxRecommendationTextView.setText("It's too bright to have a good quality sleep.");
            }
            else{
                luxRecommendationTextView.setText("The lux value in the room is optimal for a good quality sleep.");
            }
        }
        else if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            float humidity = event.values[0];
            //_mainActivity.sendNotification("Humidity Change", "XDDDDDDprosze");
            TextView humidityTextView = findViewById(R.id.humidity_textview);
            TextView percentRecommendationTextView = findViewById(R.id.percent_recommendation_textview);
            humidityTextView.setText(humidity + "%");

            if(Float.parseFloat((String.valueOf(humidity)))>60){
                percentRecommendationTextView.setText("Humidity is too high to have a good quality sleep.");
            }
            else{
                percentRecommendationTextView.setText("Humidity in the room is optimal for a good quality sleep.");
            }
        }

        else if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {

            float temperature = event.values[0];

            TextView temperatureTextView = findViewById(R.id.temperature_textview);
            TextView celsiusRecommendationTextView = findViewById(R.id.celsius_recommendation_textview);
            temperatureTextView.setText(temperature + "C");

            if(Float.parseFloat((String.valueOf(temperature)))>19){
                celsiusRecommendationTextView.setText("Room temperature is too high to have a good quality sleep.");
            }
            else{
                celsiusRecommendationTextView.setText("Temperature in the room is optimal for a good quality sleep.");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // might use it, might not, no ale poki co useless dla nas
    }

    @Override
    protected void onResume() {
        super.onResume();
        _sensorManager.registerListener(this, _lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        _sensorManager.registerListener(this, _humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        _sensorManager.registerListener(this, _temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _sensorManager.unregisterListener(this);
    }
}
