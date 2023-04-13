package com.example.myapplication.Details_setting_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.EpisodeAdapter;
import com.example.myapplication.HistoryAdapter;
import com.example.myapplication.Model.HistoryModel;
import com.example.myapplication.Model.MovieModel;
import com.example.myapplication.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class History_Activity extends AppCompatActivity  {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    FirebaseFirestore db;


    ArrayList<HistoryModel> historyModels;

    HistoryAdapter historyAdapter;


    RecyclerView HistoryWatching;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        storageRef = storage.getReference();
        FirebaseApp.initializeApp(this);


        historyModels = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyModels);

        HistoryWatching = findViewById(R.id.rv_HistoryWatching);
        HistoryWatching.setLayoutManager(new GridLayoutManager(this,3));
        HistoryWatching.setAdapter(historyAdapter);
        db = FirebaseFirestore.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference filmsRef = db.collection("Film");
        loadHistoryData();
    }
    private void loadHistoryData(){
        db.collection("ViewHistory").get().addOnCompleteListener(task -> {
           for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
               String his = documentSnapshot.get("history").toString();
               String link = documentSnapshot.get("link").toString();
               String title = documentSnapshot.get("title").toString();
               String thumb = documentSnapshot.get("thumb").toString();
               historyModels.add(new HistoryModel(his,link,title,thumb));
           }
            historyAdapter.notifyDataSetChanged();
        });
    }

}