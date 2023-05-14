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
import com.example.myapplication.Model.FavouriteModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyVH> {
    FirebaseFirestore db;
    ArrayList<FavouriteModel> favouriteModels;


    public FavouriteAdapter(ArrayList<FavouriteModel> favouriteModels) {
        this.favouriteModels = favouriteModels;
    }

    @NonNull
    @Override
    public FavouriteAdapter.MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.movie_card,parent,false);
        return new MyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.MyVH holder, int position) {
        FavouriteModel favouriteModel = favouriteModels.get(position);
        holder.movie_title_Fav.setText(favouriteModel.getTitle());
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child(favouriteModel.getThumb());
        try {
            File file = File.createTempFile("image", "jpg");
            pathReference.getFile(file)
                    .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            holder.imageViewFav.setImageBitmap(decodeFile(file.getPath()));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("DCE",e.getMessage());
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        holder.imageViewFav.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), PlayerActivity.class);
            i.putExtra("vid", favouriteModel.getLink());
            holder.itemView.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {return favouriteModels.size();}

    public class MyVH extends RecyclerView.ViewHolder {
        ImageView imageViewFav;
        TextView  movie_title_Fav;
        public MyVH(@NonNull View itemView) {

            super(itemView);
            imageViewFav = itemView.findViewById(R.id.imageView);
            movie_title_Fav = itemView.findViewById(R.id.movie_title);
        }
    }
}