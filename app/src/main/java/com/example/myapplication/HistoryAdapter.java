package com.example.myapplication;

import static android.graphics.BitmapFactory.decodeFile;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.HistoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyVH> {
    FirebaseFirestore db;
    List<HistoryAdapter> historyAdapters;
    ArrayList<HistoryModel> historyModels;

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.movie_card,parent,false);
        return new MyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH holder, int position) {
        HistoryModel historyModel =  historyModels.get(position);
        holder.textView.setText(historyModel.getTitle());
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child(historyModel.getThumb());

        db = FirebaseFirestore.getInstance();
        CollectionReference filmsRef = db.collection("Film");
        try{
            File file = File.createTempFile("episode_image", "jpg");
            pathReference.getFile(file)
                    .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            holder.imageView.setImageBitmap(decodeFile(file.getPath()));
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
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class MyVH extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.movie_title);
        }
    }

}
