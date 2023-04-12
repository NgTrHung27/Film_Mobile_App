package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().show();

        storageReference=FirebaseStorage.getInstance().getReference().child("image/BOXER.jpg");

        try {
            final File localFile=File.createTempFile("BOXER","jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(MainActivity2.this, "Success", Toast.LENGTH_SHORT).show();
                        Bitmap bitmap=BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        ((ImageView)findViewById(R.id.imageView)).setImageBitmap(bitmap);
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity2.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}