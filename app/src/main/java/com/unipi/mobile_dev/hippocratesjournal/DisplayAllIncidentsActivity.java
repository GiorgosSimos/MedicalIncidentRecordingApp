package com.unipi.mobile_dev.hippocratesjournal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

    private IncidentAdapter incidentAdapter;
    private List<Incident> incidentList;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_incidents);
        //Initialize the RecyclerView and the incident list
        RecyclerView recyclerView = findViewById(R.id.incidentsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        incidentList = new ArrayList<>();
        incidentAdapter = new IncidentAdapter(this, incidentList);
        recyclerView.setAdapter(incidentAdapter);

        // Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                    String key = dataSnapshot.getKey();

                    // Store the incident data along with the key
                    assert incident != null;
                    incident.setKey(key);
                    incidentList.add(incident); // Add each incident to the list
                }
                incidentAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showAlert("Loading of all incidents failed", error.getMessage());
            }
        });
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(DisplayAllIncidentsActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", ((dialog, which) -> dialog.dismiss()))
                .show();
    }
}