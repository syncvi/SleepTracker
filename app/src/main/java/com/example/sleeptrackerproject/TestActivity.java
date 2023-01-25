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
        String sessionName = i.getStringExtra("NAME");
        if(sessionName == null){
            ((TextView)findViewById(R.id.textView)).setText("Name of the session");
        }
        else {
            ((TextView)findViewById(R.id.textView)).setText(sessionName);
        }
    }
}