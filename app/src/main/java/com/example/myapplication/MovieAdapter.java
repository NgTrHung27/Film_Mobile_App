package com.example.myapplication;

import static android.graphics.BitmapFactory.decodeFile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.DetailsActivity;
import com.example.myapplication.Model.MovieModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyVH> {
    ArrayList<MovieModel> movieModels;

    public MovieAdapter(ArrayList<MovieModel> movieModels){
        this.movieModels = movieModels;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.movie_card,parent,false);
        return new MyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH holder, int position) {
        MovieModel movieModel = movieModels.get(position);
        holder.textView.setText(movieModel.getTitle());
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child(movieModel.getThumb());
        try{
            File file = File.createTempFile("image", "jpg");
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

        holder.imageView.setOnClickListener(view -> {
            //when click send data to details activity
            Intent sendData2Detail = new Intent(holder.imageView.getContext(), DetailsActivity.class);
            sendData2Detail.putExtra("title",movieModels.get(position).getTitle());
            sendData2Detail.putExtra("country",movieModels.get(position).getCountry());
            sendData2Detail.putExtra("cover",movieModels.get(position).getCover());
            sendData2Detail.putExtra("desc",movieModels.get(position).getDescription());
            sendData2Detail.putExtra("eps",movieModels.get(position).getEpisode());
            sendData2Detail.putExtra("length",movieModels.get(position).getLength());
            sendData2Detail.putExtra("link",movieModels.get(position).getLink());
            sendData2Detail.putExtra("rating",movieModels.get(position).getRating());
            sendData2Detail.putExtra("cast",movieModels.get(position).getCast());
            sendData2Detail.putExtra("cate",movieModels.get(position).getCate());

            //transition animation 2 detail
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity)holder.itemView.getContext(),holder.imageView,
                            "imageMain");
            //sharedElementName is the same as xml file (imageMain)
            holder.itemView.getContext().startActivity(sendData2Detail,optionsCompat.toBundle());
        });

    }

    @Override
    public int getItemCount() {
        return movieModels.size();
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
