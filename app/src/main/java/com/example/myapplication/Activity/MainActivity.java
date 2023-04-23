package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.Adapter;
import com.example.myapplication.Model.MovieModel;
import com.example.myapplication.MovieAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Adapter.Listener {

    RecyclerView rvStudent;
    Adapter studentAdapter;
    ArrayList<Student> students;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        rvStudent = findViewById(R.id.rvList);
        students = new ArrayList<>();

        studentAdapter = new Adapter(MainActivity.this, students);
        rvStudent.setAdapter(studentAdapter);

        rvStudent.setLayoutManager((new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false)));
        rvStudent.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        loadData();
    }

    private void loadData() {
        db.collection("SinhVien")
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String ID = document.getId();
                        String NAME = document.getString("Name");
                        Student student = new Student(ID, NAME);
                        students.add(student);
                    }
                    studentAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void setOnDeleteClick(Student st) {
    }
    @Override
    public void setOnEditClick(Student st) {
    }
}