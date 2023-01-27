package com.example.sleeptrackerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WeatherActivity extends AppCompatActivity {
    private TextView _temperatureTextView;
    private LocationManager _locationManager;
    private LocationListener _locationListener;
    private double temperature;
    private static final String WEATHER_URL_BASE = "https://api.openweathermap.org/data/2.5/weather?lat=";
    private Executor _executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        _temperatureTextView = (TextView) findViewById(R.id.temperature_textview);

        _locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // onLocationChanged lambda
        _locationListener = location -> {
            // making a request to the weather API to get the current weather for this location
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String apiKey = "9a408f1ff1beb4273b2c780b9b805225";
            String weatherUrl = WEATHER_URL_BASE + latitude + "&lon=" + longitude + "&appid=" + apiKey;
            //executor so it does stuff in the background and doesn't freeze an entire app
            _executor.execute(() -> getWeather(weatherUrl));

            if(temperature-273.15<10.0){
                NotificationHelper.sendNotification(this,"Good morning!", "It's a bit chilly outside, better prepare some warm clothes!");
            }

            else if (temperature-273.15>15.0){
                NotificationHelper.sendNotification(this,"Good morning!", "Hey! The sun's out. Better catch up!");
            }

            else if (temperature-273.15>20.0){
                NotificationHelper.sendNotification(this,"Good morning!", "It's hot today! Remember to drink a lot of water.");
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // check the location perms
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request the location perms
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            // start listening for location updates if we have permissions
            _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, _locationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop listening for location updates when the activity is not visible
        _locationManager.removeUpdates(_locationListener);
    }

    private void getWeather(String weatherUrl) {
        try {
            URL url = new URL(weatherUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            JSONObject json = new JSONObject(result.toString());
            JSONObject main = json.getJSONObject("main");

            temperature = main.getDouble("temp") - 273.15;
            final String temperatureString = String.format("%.2f", temperature) + " \u00B0C";

            runOnUiThread(() -> _temperatureTextView.setText(temperatureString));

        }

        catch (Exception e) {e.printStackTrace();}
    }
}
