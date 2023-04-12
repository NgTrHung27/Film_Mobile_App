package com.example.myapplication;

import static android.content.ContentValues.TAG;
import static android.graphics.BitmapFactory.decodeFile;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.DetailsActivity;
import com.example.myapplication.Activity.PlayerActivity;
import com.example.myapplication.Details_setting_activity.History_Activity;
import com.example.myapplication.Model.EpisodeModel;
import com.example.myapplication.Model.FeatureModel;
import com.example.myapplication.Model.MovieModel;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MyVH> {
    List<EpisodeModel> episodeModels;
    List<MovieModel> movieModels;
    FirebaseFirestore db;

    public EpisodeAdapter(List<EpisodeModel> models) {
        this.episodeModels = models;
    }

    @NonNull
    @Override
    public EpisodeAdapter.MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.part_item,parent,false);

        return new MyVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.MyVH holder, int position) {
        EpisodeModel episodeModel = episodeModels.get(position);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child(episodeModel.getUrl());
        db = FirebaseFirestore.getInstance();
        CollectionReference filmsRef = db.collection("Film");
        try{
            File file = File.createTempFile("episode_image", "jpg");
            pathReference.getFile(file)
                    .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            holder.part_image.setImageBitmap(decodeFile(file.getPath()));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ABC",e.getMessage());
                        }
                    });
        }catch (IOException e)
        {
            throw new RuntimeException();
        }
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), PlayerActivity.class);
            i.putExtra("vid",episodeModel.getVidurl());
            holder.itemView.getContext().startActivity(i);

            // Lấy tất cả các documents trong collection "Film"
            filmsRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Duyệt qua tất cả các documents trong collection "Film"
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Lấy ID của document hiện tại
                        String documentId = document.getId();

                        // Cập nhật giá trị của trường "History" trong document hiện tại
                        DocumentReference currentDocRef = db.collection("Film").document(documentId);
                        currentDocRef.update("History", "1");
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });

            //when click send data to details activity
            Intent sendData2History = new Intent(holder.itemView.getContext(), History_Activity.class);
            sendData2History.putExtra("title",movieModels.get(position).getTitle());
            sendData2History.putExtra("history",movieModels.get(position).getTitle());
            sendData2History.putExtra("link",movieModels.get(position).getLink());

            //transition animation 2 detail
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity)holder.itemView.getContext(),holder.itemView,
                            "imageMain");
            //sharedElementName is the same as xml file (imageMain)
            holder.itemView.getContext().startActivity(sendData2History,optionsCompat.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return episodeModels.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        ImageView part_image;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            part_image=itemView.findViewById(R.id.part_image);
        }
    }
}
