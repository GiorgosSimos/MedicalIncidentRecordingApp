package com.unipi.mobile_dev.hippocratesjournal;

import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private IncidentAdapter incidentAdapter;
    private List<Incident> incidentList;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        database = FirebaseDatabase.getInstance();

        RecyclerView recyclerView = findViewById(R.id.searchResultsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        incidentList = new ArrayList<>();
        incidentAdapter = new IncidentAdapter(this, incidentList);
        recyclerView.setAdapter(incidentAdapter);

        //Get the search query data from the Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String searchCriteria = extras.getString("searchCriteria");
            String searchValue = extras.getString("searchValue");
            executeSearch(searchCriteria, searchValue);
        }
    }

    //Function to perform search in Realtime Database
    private void executeSearch(String criteria, String value) {
        reference = database.getReference("incidents");
        Query query;
        if (criteria.equals("dateOfExamination")) {// For search based on date of examination, an exact match is required
            query = reference.orderByChild(criteria).equalTo(value);
        } else { //For search based on name, diagnosis a partial match match is required
            query = reference.orderByChild(criteria).startAt(value).endAt(value + "\uf8ff");
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    incidentList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Incident incident = dataSnapshot.getValue(Incident.class);
                        String key = dataSnapshot.getKey();

                        // Store the incident data along with the key
                        assert incident != null;
                        incident.setKey(key);
                        incidentList.add(incident);
                    }
                    incidentAdapter.notifyDataSetChanged();
                } else { // Redirect to search screen
                    redirectAlert(getString(R.string.no_results), getString(R.string.no_results_descr));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showAlert(getString(R.string.search_failed), error.getMessage());
            }
        });
    }

    private void redirectAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Redirect to Search Activity after clicking OK
                    Intent intent = new Intent(this, SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(SearchResultsActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", ((dialog, which) -> dialog.dismiss()))
                .show();
    }
}