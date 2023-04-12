package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.EpisodeAdapter;
import com.example.myapplication.Model.EpisodeModel;
import com.example.myapplication.Model.MovieModel;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    FirebaseFirestore db;
    List<EpisodeModel> episodeModels;
    EpisodeAdapter episodeAdapter;

    RecyclerView part_recycler_view;

    ImageView cover;
    TextView title,desc,cast,length,rating,eps,country,cate;
    String title_movie;
    String cast_movie;
    String country_movie;
    String cover_movie;
    String desc_movie;
    String eps_movie;
    String length_movie;
    String link_movie;
    String rating_movie;
    String cate_movie;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        cate = findViewById(R.id.cate);
        cover = findViewById(R.id.cover);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.description);
        cast = findViewById(R.id.cast);
        country = findViewById(R.id.country);
        length = findViewById(R.id.length);
        rating = findViewById(R.id.rating);
        eps = findViewById(R.id.eps);
        part_recycler_view = findViewById(R.id.rv_eps);

        title_movie = getIntent().getStringExtra("title");
        desc_movie = getIntent().getStringExtra("desc");
        cast_movie = getIntent().getStringExtra("cast");
        country_movie = getIntent().getStringExtra("country");
        cover_movie = getIntent().getStringExtra("cover");
        eps_movie = getIntent().getStringExtra("eps");
        length_movie = getIntent().getStringExtra("length");
        link_movie = getIntent().getStringExtra("link");
        rating_movie = getIntent().getStringExtra("rating");
        cate_movie = getIntent().getStringExtra("cate");

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title_movie);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(this).load(cover_movie).into(cover);
        cover.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));

        title.setText(title_movie);
        desc.setText("Mô tả: " + desc_movie);
        cast.setText("Diễn viên: "+cast_movie);
        cate.setText("Thể loại: "+cate_movie);
        length.setText(length_movie);
        rating.setText(rating_movie);
        eps.setText("Số tập: "+eps_movie);
        country.setText(country_movie);

        loadEps();
    }

    private void loadEps() {
        db = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        part_recycler_view.setLayoutManager(layoutManager);

        episodeModels = new ArrayList<>();
        episodeAdapter = new EpisodeAdapter(episodeModels);
        part_recycler_view.setAdapter(episodeAdapter);

        db.collection("Link").document(link_movie).collection("0").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                String url = documentSnapshot.get("url").toString();
                String vidurl = documentSnapshot.get("vidurl").toString();
                episodeModels.add(new EpisodeModel(url,vidurl));
            }
            episodeAdapter.notifyDataSetChanged();

        }).addOnFailureListener(e ->
                Toast.makeText(DetailsActivity.this, "ERROR", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}