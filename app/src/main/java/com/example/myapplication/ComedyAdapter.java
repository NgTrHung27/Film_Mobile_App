package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.DetailsActivity;
import com.example.myapplication.Model.ComedyModel;

import java.util.List;

public class ComedyAdapter extends RecyclerView.Adapter<ComedyAdapter.MyViewHoler> {
    List<ComedyModel> dataModels;

    public ComedyAdapter(List<ComedyModel> dataModels){
        this.dataModels = dataModels;
    }

    @NonNull
    @Override
    public ComedyAdapter.MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.movie_card,parent,false);
        return new MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComedyAdapter.MyViewHoler holder, int position) {
        holder.textView.setText(dataModels.get(position).getCtitle());
        Glide.with(holder.itemView.getContext()).
                load(dataModels.get(position).getCthumb()).into(holder.imageView);

        holder.imageView.setOnClickListener(view -> {
            //when click send data to details activity
            Intent sendData2Detail = new Intent(holder.imageView.getContext(), DetailsActivity.class);
            sendData2Detail.putExtra("title",dataModels.get(position).getCtitle());
            sendData2Detail.putExtra("country",dataModels.get(position).getCcountry());
            sendData2Detail.putExtra("cover",dataModels.get(position).getCcover());
            sendData2Detail.putExtra("desc",dataModels.get(position).getCdesc());
            sendData2Detail.putExtra("eps",dataModels.get(position).getCeps());
            sendData2Detail.putExtra("length",dataModels.get(position).getClength());
            sendData2Detail.putExtra("link",dataModels.get(position).getClink());
            sendData2Detail.putExtra("rating",dataModels.get(position).getCrating());
            //sendData2Detail.putExtra("thumb",dataModels.get(position).getAthumb());
            sendData2Detail.putExtra("cast",dataModels.get(position).getCcast());

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
        return dataModels.size();
    }

    public class MyViewHoler extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public MyViewHoler(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.movie_title);
        }
    }
}
