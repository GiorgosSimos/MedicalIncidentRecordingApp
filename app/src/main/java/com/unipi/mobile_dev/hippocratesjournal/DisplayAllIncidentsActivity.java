package com.unipi.mobile_dev.hippocratesjournal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayAllIncidentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IncidentAdapter incidentAdapter;
    private List<Incident> incidentList;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_incidents);
        //Initialize the RecyclerView and list
        recyclerView = findViewById(R.id.incidentsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        incidentList = new ArrayList<>();
        incidentAdapter = new IncidentAdapter(incidentList);
        recyclerView.setAdapter(incidentAdapter);

        // Firebase database reference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("incidents");

        //Retrieve incidents from Firebase and update the RecyclerView
        loadIncidentsFromDB();
    }

    private void loadIncidentsFromDB() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                incidentList.clear();// Clear the list each time before adding new data
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    Incident incident = dataSnapshot.getValue(Incident.class);
                    incidentList.add(incident); // Add each incident to the list
                }
                incidentAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}