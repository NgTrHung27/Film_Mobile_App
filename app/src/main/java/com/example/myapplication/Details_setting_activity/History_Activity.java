package com.example.myapplication.Details_setting_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.Model.HistoryModel;
import com.example.myapplication.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class History_Activity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;

    ArrayList<HistoryModel> historyModels;;

    FirebaseFirestore db;
    List<HistoryModel> HistoryAdapter;
    RecyclerView HistoryWatching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        storageRef = storage.getReference();

        FirebaseApp.initializeApp(this);


    }
}