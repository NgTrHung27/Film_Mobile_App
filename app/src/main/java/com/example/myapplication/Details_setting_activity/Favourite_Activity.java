package com.example.myapplication.Details_setting_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.FavouriteAdapter;
import com.example.myapplication.Model.FavouriteModel;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Favourite_Activity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db;

    ArrayList<FavouriteModel> favouriteModels;
    FavouriteAdapter favouriteAdapter;
    RecyclerView FavouriteWatching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Favourite");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(view -> finish());

        storageRef = storage.getReference();
        FirebaseApp.initializeApp(this);
        favouriteModels = new ArrayList<>();
        favouriteAdapter = new FavouriteAdapter(favouriteModels);

        FavouriteWatching = findViewById(R.id.rv_FavWatching);
        FavouriteWatching.setLayoutManager(new GridLayoutManager(this,3));
        FavouriteWatching.setAdapter(favouriteAdapter);
        db = FirebaseFirestore.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        loadFavouriteData();
        ImageButton btnClearFav = findViewById(R.id.btn_Clear_Fav);
        btnClearFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Favourite").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            String document = documentSnapshot.getId();
                            if (document.equals("1")){
                                continue;
                            }
                            db.collection("Favourite").document(document).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Favourite_Activity.this, "Delete Success!!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            favouriteModels.clear();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Favourite_Activity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void loadFavouriteData(){
        db.collection("Favourite").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                if (documentSnapshot.getId().equals("1")){
                    continue;
                }
                String link = documentSnapshot.get("link").toString();
                String title = documentSnapshot.get("title").toString();
                String thumb = documentSnapshot.get("thumb").toString();
                favouriteModels.add(new FavouriteModel(link,title,thumb));
            }
            favouriteAdapter.notifyDataSetChanged();
        });
    }
}