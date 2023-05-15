package com.example.myapplication;

import static android.content.ContentValues.TAG;
import static android.graphics.BitmapFactory.decodeFile;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.myapplication.Model.FavouriteModel;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MyVH> {
    List<EpisodeModel> episodeModels;
    FirebaseFirestore db;
    private String UUID_Favorite = "";

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
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ABC",e.getMessage());
                        }
                    });
        }catch (IOException e)
        {
            throw new RuntimeException();
        }
        holder.part_image.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), PlayerActivity.class);
            i.putExtra("vid", episodeModel.getVidurl());
            holder.itemView.getContext().startActivity(i);
            HistoryModel history = new HistoryModel(episodeModel.getVidurl(),"abc", episodeModel.getUrl());
            saveToFireStore(history);
        });

//        holder.SaveFav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FavouriteModel favouriteModel = new FavouriteModel(episodeModel.getVidurl(),"Test", episodeModel.getUrl());
//                saveFavToFireStore(favouriteModel);
//            }
//        });
        holder.checkFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        FavouriteModel favouriteModel = new FavouriteModel(episodeModel.getVidurl(),"Test", episodeModel.getUrl());
                if (isChecked) {
                    // Người dùng đã click vào checkbox, lưu dữ liệu lên Firestore
                    saveFavToFireStore(favouriteModel);
                    Toast.makeText(buttonView.getContext(), "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    // Người dùng đã bỏ chọn checkbox, xóa dữ liệu khỏi Firestore
                    deleteFavFromFirestore(favouriteModel);
                    Toast.makeText(buttonView.getContext(), "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public void saveToFireStore(HistoryModel History) {
        if (!History.getTitle().isEmpty() && !History.getThumb().isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", History.getTitle());
            map.put("thumb", History.getThumb());
            map.put("link", History.getLink());
//            map.put("history", History.getHistory());
            String id = UUID.randomUUID().toString();
            db.collection("ViewHistory").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                }
            });
        }
    }

    public void saveFavToFireStore(FavouriteModel favouriteModel) {
        if (!favouriteModel.getTitle().isEmpty() && !favouriteModel.getThumb().isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", favouriteModel.getTitle());
            map.put("thumb", favouriteModel.getThumb());
            map.put("link", favouriteModel.getLink());
            UUID_Favorite = UUID.randomUUID().toString();
            map.put("id", UUID_Favorite);
            favouriteModel.setId(UUID_Favorite);
            db.collection("Favourite").document(favouriteModel.getId()).set(map);
            notifyDataSetChanged();
        }
    }
    public void deleteFavFromFirestore(FavouriteModel favouriteModel){
        db.collection("Favourite/").document(UUID_Favorite).delete();
    }

    @Override
    public int getItemCount() {
        return episodeModels.size();
    }

    public class MyVH extends RecyclerView.ViewHolder {
        ImageView part_image;
//        ImageButton SaveFav;
        CheckBox checkFav;

        public MyVH(@NonNull View itemView) {
            super(itemView);
            part_image=itemView.findViewById(R.id.part_image);
            checkFav = itemView.findViewById(R.id.cbHeart);
//            SaveFav = itemView.findViewById(R.id.btn_SaveFav);
        }
    }
}
