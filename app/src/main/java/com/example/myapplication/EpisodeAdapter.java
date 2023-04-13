package com.example.myapplication;

import static android.content.ContentValues.TAG;
import static android.graphics.BitmapFactory.decodeFile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.Model;
import com.example.myapplication.Activity.DetailsActivity;
import com.example.myapplication.Activity.PlayerActivity;
import com.example.myapplication.Details_setting_activity.History_Activity;
import com.example.myapplication.Model.EpisodeModel;
import com.example.myapplication.Model.FeatureModel;
import com.example.myapplication.Model.HistoryModel;
import com.example.myapplication.Model.MovieModel;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MyVH> {
    List<EpisodeModel> episodeModels;
    List<MovieModel> movieModels;
    FirebaseFirestore db;

    private AdapterView.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String title, String thumb, String link);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = (AdapterView.OnItemClickListener) listener;
    }

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
            HistoryModel history = new HistoryModel("ssss", episodeModel.getVidurl(), "tttt", episodeModel.getUrl());
            saveToFireStore(history);

            // Lấy tất cả các documents trong collection "Film"
//            filmsRef.get().addOnCompleteListener(task -> {
//                /// them du lieu len fs
////                if (task.isSuccessful()) {
////                    // Duyệt qua tất cả các documents trong collection "Film"
////                    for (QueryDocumentSnapshot document : task.getResult()) {
////                        // Lấy ID của document hiện tại
////                        String documentId = document.getId();
////
////                        // Cập nhật giá trị của trường "History" trong document hiện tại
////                        DocumentReference currentDocRef = db.collection("Film").document(documentId);
////                        currentDocRef.update("History", "1");
////                    }
////                } else {
////                    Log.d(TAG, "Error getting documents: ", task.getException());
////                }
//            });
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mListener != null) {
//                        mListener.onItemClick(movieModels.get(position).getTitle(), movieModels.get(position).getLink(), movieModels.get(position).getThumb());
//                    }
//                }
//            });


//            if (movieModels != null && movieModels.size() > position) {
//                Intent sendData2History = new Intent(holder.itemView.getContext(), History_Activity.class);
//                sendData2History.putExtra("title",movieModels.get(position).getTitle());
//                sendData2History.putExtra("history",movieModels.get(position).getTitle());
//                sendData2History.putExtra("link",movieModels.get(position).getLink());
//
//                //transition animation 2 detail
//                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
//                        .makeSceneTransitionAnimation((Activity)holder.itemView.getContext(),holder.itemView,
//                                "imageMain");
//                //sharedElementName is the same as xml file (imageMain)
//                holder.itemView.getContext().startActivity(sendData2History,optionsCompat.toBundle());
//            } else {
//                Log.e(TAG, "movieModels is null or size <= position");
//            }

        });
    }
    public void saveToFireStore(HistoryModel History) {
        if (!History.getTitle().isEmpty() && !History.getThumb().isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", History.getTitle());
            map.put("thumb", History.getThumb());
            map.put("link", History.getLink());
            map.put("history", History.getHistory());
            String id = UUID.randomUUID().toString();
            db.collection("ViewHistory").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                }
            });
        }
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
