package com.example.myapplication.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.graphics.Matrix;
import android.icu.number.Scale;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.myapplication.Adapter;
import com.example.myapplication.AnimeAdapter;
import com.example.myapplication.CartoonAdapter;
import com.example.myapplication.FeatureAdapter;
import com.example.myapplication.Fragment.SearchFragment;
import com.example.myapplication.Fragment.SettingFragment;
import com.example.myapplication.Model.AnimeModel;
import com.example.myapplication.Model.FeatureModel;
import com.example.myapplication.Model.MovieModel;
import com.example.myapplication.MovieAdapter;
import com.example.myapplication.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.checkerframework.checker.units.qual.A;

import java.lang.ref.Reference;
import java.util.ArrayList;

public class MovieTest extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    FirebaseFirestore db;

    ArrayList<MovieModel> movieModels;
    MovieAdapter movieAdapter;

    ArrayList<FeatureModel> featureModels;
    FeatureAdapter featureAdapter;

    RecyclerView rv_Movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_test);

        storageRef = storage.getReference();

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setLogo(R.drawable.toolbar_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //ClickListener for fragment
        toolbar.setOnMenuItemClickListener(item -> {
            display(item.getItemId());
            return true;
        });

        FirebaseApp.initializeApp(this);
        SliderView sliderView = findViewById(R.id.sliderView);
        featureAdapter = new FeatureAdapter(this);
        sliderView.setSliderAdapter(featureAdapter);
        sliderView.setCurrentPagePosition(0);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SCALE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycle(true);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(6);
        renewItems(sliderView);

        movieModels = new ArrayList<>();
        movieAdapter = new MovieAdapter(movieModels);
        db = FirebaseFirestore.getInstance();

        rv_Movie = findViewById(R.id.rv_Movie);
        rv_Movie.setAdapter(movieAdapter);
        rv_Movie.setLayoutManager(new GridLayoutManager(this,3));

//        rv_cartoon = findViewById(R.id.rv_Cartoon);
//        rv_cartoon.setAdapter(movieAdapter);
//        rv_cartoon.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        loadFeatureSlider();
        loadFilmData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item,menu);
        //region Search
        MenuItem item = menu.findItem(R.id.mn_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //call when press search button
                seachData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //call when type letter
                if (s.isEmpty()){
                    movieModels.clear();
                    loadFilmData();
                }else {
                    rv_Movie.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        //endregion
        return super.onCreateOptionsMenu(menu);
    }

    void seachData(String s){
        db.collection("Film").whereEqualTo("Search",s.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        movieModels.clear();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            String title = documentSnapshot.get("Title").toString();
                            String cast = documentSnapshot.get("Cast").toString();
                            String cover = documentSnapshot.get("Cover").toString();
                            String desc = documentSnapshot.get("Desc").toString();
                            String eps = documentSnapshot.get("Eps").toString();
                            String his = documentSnapshot.get("History").toString();
                            String length = documentSnapshot.get("Length").toString();
                            String link = documentSnapshot.get("Link").toString();
                            String rate = documentSnapshot.get("Rate").toString();
                            String cate = documentSnapshot.get("Cate").toString();
                            String thumb = documentSnapshot.get("Thumb").toString();
                            String country = documentSnapshot.get("Country").toString();
                            movieModels.add(new MovieModel(cast, country, cover, desc, eps, length, link, rate, title, thumb, his, cate));
                        }
                        movieAdapter = new MovieAdapter(movieModels);
                        rv_Movie.setAdapter(movieAdapter);
                    }
                }).addOnFailureListener(e -> {
                });
    }

    void display(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.mn_setting:
                fragment = new SettingFragment();
                break;
            case R.id.mn_search:
                fragment = new SearchFragment();
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .replace(R.id.content,fragment,null).addToBackStack("fragment_setting");
        //replace framelayout(id content)
//        ft.replace(R.id.content,fragment).addToBackStack(null).commit();
        //save the display
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData() {
        //implement in next video
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadFilmData() {
        db.collection("Film").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                String title = documentSnapshot.get("Title").toString();
                String cast = documentSnapshot.get("Cast").toString();
                String cover = documentSnapshot.get("Cover").toString();
                String desc = documentSnapshot.get("Desc").toString();
                String eps = documentSnapshot.get("Eps").toString();
                String his = documentSnapshot.get("History").toString();
                String length = documentSnapshot.get("Length").toString();
                String link = documentSnapshot.get("Link").toString();
                String rate = documentSnapshot.get("Rate").toString();
                String cate = documentSnapshot.get("Cate").toString();
                String thumb = documentSnapshot.get("Thumb").toString();
                String country = documentSnapshot.get("Country").toString();
                movieModels.add(new MovieModel(cast, country, cover, desc, eps, length, link, rate, title, thumb, his, cate));
            }
            movieAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "ERORR!!!", Toast.LENGTH_LONG).show();
        });
    }

    private void loadFeatureSlider() {
        db.collection("Feature").get().addOnCompleteListener(task -> {
            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                String Title = queryDocumentSnapshot.get("Title").toString();
                String Thumb = queryDocumentSnapshot.get("Thumb").toString();
                String Vid = queryDocumentSnapshot.get("Vid").toString();
                featureModels.add(new FeatureModel(Title,Thumb,Vid));
            }
            featureAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Toast.makeText(MovieTest.this, "Error!!!", Toast.LENGTH_SHORT).show());
    }

    public void renewItems(View view){
        featureModels = new ArrayList<>();
        FeatureModel dataItems = new FeatureModel();
        featureModels.add(dataItems);
        featureAdapter.renewItems(featureModels);
        featureAdapter.deleteItems(0);
    }
}