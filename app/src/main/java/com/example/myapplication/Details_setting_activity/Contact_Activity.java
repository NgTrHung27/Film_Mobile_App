package com.example.myapplication.Details_setting_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.myapplication.R;

public class Contact_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contact");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}