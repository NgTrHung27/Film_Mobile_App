package com.example.myapplication.Details_setting_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;

public class Info_Account_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_account);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}