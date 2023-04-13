package com.example.myapplication;

import static android.graphics.BitmapFactory.decodeFile;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.PlayerActivity;
import com.example.myapplication.Model.HistoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyVH> {
    FirebaseFirestore db;
    ArrayList<HistoryModel> historyModels;

    public HistoryAdapter(ArrayList<HistoryModel> historyModels) {this.historyModels = historyModels;}


    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.movie_card,parent,false);
        return new MyVH(view);
    }

    @Override
//    public void onBindViewHolder(@NonNull MyVH holder, int position) {
//        HistoryModel historyModel =  historyModels.get(position);
//        holder.movie_title_History.setText(historyModel.getTitle());
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        StorageReference pathReference = storageRef.child(historyModel.getThumb());
//
//        db = FirebaseFirestore.getInstance();
//        CollectionReference filmsRef = db.collection("Film");
//        try{
//            File file = File.createTempFile("history_image", "jpg");
//            pathReference.getFile(file)
//                    .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                            holder.imageViewHistory.setImageBitmap(decodeFile(file.getPath()));
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d("ABC",e.getMessage());
//                        }
//                    });
//        }catch (IOException e)
//        {
//            throw new RuntimeException();
//        }
//    }
    public void onBindViewHolder(@NonNull MyVH holder, int position) {
        HistoryModel historyModel =  historyModels.get(position);
        holder.movie_title_History.setText(historyModel.getTitle());
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child(historyModel.getThumb());

        try{
            File file = File.createTempFile("image", "jpg");
            pathReference.getFile(file)
                    .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            holder.imageViewHistory.setImageBitmap(decodeFile(file.getPath()));
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
        holder.imageViewHistory.setOnClickListener(view -> {
                    Intent i = new Intent(view.getContext(), PlayerActivity.class);
                    i.putExtra("vid", historyModel.getLink());
                    holder.itemView.getContext().startActivity(i);
                });
        db = FirebaseFirestore.getInstance();
        CollectionReference historyRef = db.collection("ViewHistory");
        historyRef.whereEqualTo("history", 1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        HistoryModel historyModel = document.toObject(HistoryModel.class);
                        historyModels.add(historyModel);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return historyModels.size();
    }


    public class MyVH extends RecyclerView.ViewHolder{
        ImageView imageViewHistory;
        TextView movie_title_History;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            imageViewHistory = itemView.findViewById(R.id.imageView);
            movie_title_History = itemView.findViewById(R.id.movie_title);
        }
    }

}
