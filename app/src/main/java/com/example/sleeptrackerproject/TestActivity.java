package com.example.sleeptrackerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.sleeptrackerproject.database.SleepSessionDao;
import com.example.sleeptrackerproject.database.SleepSessionDatabase;


public class TestActivity extends AppCompatActivity {

    private RatingBar _ratingBar;
    private EditText _textField;
    private SleepSessionData _sessionData;
    private SleepSessionDao _sessionDataDao;
    private int sessionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        _ratingBar = findViewById(R.id.session_rating);
        _textField = findViewById(R.id.session_description_edittext);

        Intent i = getIntent();
        sessionNumber = i.getIntExtra("SESSION_NUMBER", 0);
        String sessionName = "Session " + sessionNumber;
        ((TextView) findViewById(R.id.textView)).setText(sessionName);

        //new Thread(this::setParameters).start();


    }

    public void setParameters(){
        _sessionDataDao = SleepSessionDatabase.getInstance(this).sleepSessionDao();
        _sessionData = _sessionDataDao.getSessionData(sessionNumber);
        _ratingBar.setRating(_sessionData.getRating());
        _textField.setText(_sessionData.getText());
    }

    public void onSaveButtonClick(View v) {
        float rating = _ratingBar.getRating();
        String text = _textField.getText().toString();

        _sessionData.setRating(rating);
        _sessionData.setText(text);

        _sessionDataDao.updateData(_sessionData);

        Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();
    }

}