package com.example.myapplication.Details_setting_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.EpisodeAdapter;
import com.example.myapplication.HistoryAdapter;
import com.example.myapplication.Model.HistoryModel;
import com.example.myapplication.Model.MovieModel;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db;

    ArrayList<HistoryModel> historyModels;
    HistoryAdapter historyAdapter;
    RecyclerView HistoryWatching;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(view -> finish());

        storageRef = storage.getReference();
        FirebaseApp.initializeApp(this);
        historyModels = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyModels);

        HistoryWatching = findViewById(R.id.rv_HistoryWatching);
        HistoryWatching.setLayoutManager(new GridLayoutManager(this,3));
        HistoryWatching.setAdapter(historyAdapter);
        db = FirebaseFirestore.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        loadHistoryData();

        ImageButton btnClearHis = findViewById(R.id.btn_Clear);
        btnClearHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("ViewHistory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            String document = documentSnapshot.getId();
                            if (document.equals("0")){
                                continue;
                            }
                            db.collection("ViewHistory").document(document).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(History_Activity.this, "Delete Success!!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            historyModels.clear();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(History_Activity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void loadHistoryData(){
        db.collection("ViewHistory").get().addOnCompleteListener(task -> {
           for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
               if (documentSnapshot.getId().equals("0")){
                   continue;
               }
               String link = documentSnapshot.get("link").toString();
               String title = documentSnapshot.get("title").toString();
               String thumb = documentSnapshot.get("thumb").toString();
               historyModels.add(new HistoryModel(link,title,thumb));
           }
            historyAdapter.notifyDataSetChanged();
        });
    }

}