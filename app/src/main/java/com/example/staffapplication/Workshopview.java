package com.example.staffapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class Workshopview extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fab;

    WorkshopAdapter workshopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshopview);
        recyclerView = (RecyclerView)findViewById(R.id.wrv);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<workmain> options=
                new FirebaseRecyclerOptions.Builder<workmain>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Workshop"),workmain.class)
                        .build();

        workshopAdapter = new WorkshopAdapter(options);
        recyclerView.setAdapter(workshopAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Workshopview.this,Workshop.class));
            }
        });





    }


    @Override
    protected void onStart() {
        super.onStart();
        workshopAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        workshopAdapter.stopListening();
    }

}
