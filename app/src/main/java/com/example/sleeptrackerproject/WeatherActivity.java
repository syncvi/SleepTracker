package com.example.sleeptrackerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WeatherActivity extends AppCompatActivity {
    private TextView _temperatureTextView;
    private TextView _weatherDescriptionTextView;
    private LocationManager _locationManager;
    private LocationListener _locationListener;
    private double temperature;
    private String _shortWeatherDesc;
    private boolean _sendNotiOnce = true;
    private static final String WEATHER_URL_BASE = "https://api.openweathermap.org/data/2.5/weather?lat=";
    private Executor _executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        _temperatureTextView =findViewById(R.id.temperature_textview);
        _weatherDescriptionTextView = findViewById(R.id.weather_description_textview);

        Button mapsButton = (Button) findViewById(R.id.google_maps_button);


        _locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // onLocationChanged lambda
        _locationListener = location -> {
            // making a request to the weather API to get the current weather for this location
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String apiKey = "9a408f1ff1beb4273b2c780b9b805225";
            String weatherUrl = WEATHER_URL_BASE + latitude + "&lon=" + longitude + "&appid=" + apiKey;
            mapsButton.setOnClickListener(view -> {
                view.startAnimation(myAnim);
                launchGoogleMaps(longitude, latitude);
            });
            //executor so it does stuff in the background and doesn't freeze an entire app
            _executor.execute(() -> getWeather(weatherUrl));

            if (_sendNotiOnce){
                if(temperature<10.0){
                    NotificationHelper.sendNotification(this, getString(R.string.Notification_title), getString(R.string.cold_noti));
                }

                else if (temperature>15.0 && temperature <20.0){
                    NotificationHelper.sendNotification(this, getString(R.string.Notification_title), getString(R.string.warm_noti));
                }

                else if (temperature>20.0){
                    NotificationHelper.sendNotification(this, getString(R.string.Notification_title), getString(R.string.hot_noti));
                }

                _sendNotiOnce = false;
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
            JSONObject wind = json.getJSONObject("wind");
            JSONArray weather = json.getJSONArray("weather");
            JSONObject desc = weather.getJSONObject(0);


            //System.err.println("Speed equals" + wind.getDouble("speed"));
            //System.err.println(xd);

            //rainProbability = rain.getString("description");
            temperature = main.getDouble("temp") - 273.15;
            _shortWeatherDesc = desc.getString("description");
            @SuppressLint("DefaultLocale") final String temperatureString = String.format("%.2f", temperature) + " \u00B0C";
            final String weatherDescriptionString = _shortWeatherDesc;
            //final String rainProbabilityString = "Probability of rain in 1h is" + rainProbability + " %";

            runOnUiThread(() -> {
                _temperatureTextView.setText(temperatureString);
                _weatherDescriptionTextView.setText(weatherDescriptionString);
            });

        }

        catch (Exception e) {e.printStackTrace();}
    }

    public void launchGoogleMaps(double longitude, double latitude) {
        @SuppressLint("DefaultLocale") String uri = String.format("geo:%f,%f?q=%f,%f", latitude, longitude, latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }


}
