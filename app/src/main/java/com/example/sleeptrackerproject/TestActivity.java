package com.example.sleeptrackerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent i = getIntent();
        int sessionNumber = i.getIntExtra("SESSION_NUMBER", 0);
        String sessionName = "Session " + sessionNumber;
        ((TextView)findViewById(R.id.textView)).setText(sessionName);
    }
}