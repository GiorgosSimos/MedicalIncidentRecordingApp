package com.unipi.mobile_dev.hippocratesjournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailedIncidentActivity extends AppCompatActivity {

    private TextView textViewName, textViewDob, textViewDoe, textViewGender, textViewSymptoms, textViewDiagnosis, textViewPrescription;
    FirebaseDatabase database;
    DatabaseReference reference;
    String incidentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_incident);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("incidents");

        // Initialize the views
        textViewName = findViewById(R.id.editTextIncidentName);
        textViewDob = findViewById(R.id.incidentDobEditText);
        textViewDoe = findViewById(R.id.incidentDoeEditText);
        textViewGender = findViewById(R.id.incidentGenderEditText);
        textViewSymptoms = findViewById(R.id.editTextIncidentSymptoms);
        textViewDiagnosis = findViewById(R.id.editTextIncidentDiagnosis);
        textViewPrescription = findViewById(R.id.editTextIncidentPrescription);

        // Get data from intent
        incidentId = getIntent().getStringExtra("incidentId");
        String name = getIntent().getStringExtra("name");
        String dob = getIntent().getStringExtra("dob");
        String doe = getIntent().getStringExtra("doe");
        String gender = getIntent().getStringExtra("gender");
        String symptoms = getIntent().getStringExtra("symptoms");
        String diagnosis = getIntent().getStringExtra("diagnosis");
        String prescription = getIntent().getStringExtra("prescription");

        textViewName.setText("Name: " + name);
        textViewDob.setText("Date of Birth: " + dob);
        textViewDoe.setText("Date of Examination: " + doe);
        textViewGender.setText("Gender: " + gender);
        textViewSymptoms.setText("Symptoms: " + symptoms);
        textViewDiagnosis.setText("Diagnosis: " + diagnosis);
        textViewPrescription.setText("Prescription: " + prescription);
    }

    // Delete the incident from Firebase using incidentId
    public void deleteIncident(View view) {
        // Create an AlertDialog to ask for confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedIncidentActivity.this);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Are you sure you want to delete this incident");

        // If the user clicks Yes, delete the incident
        builder.setPositiveButton("Yes", ((dialog, which) -> {
            reference.child(incidentId).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showAlert("Successful Delete","Incident deleted successfully!");
                        } else {
                            showAlert("Delete Failed","Failed to delete incident. Please try again.");
                        }
                        navigateToMainScreen();
                    });
        }));

        // If the user clicks No, nothing happens
        builder.setNegativeButton("No", ((dialog, which) -> dialog.dismiss()));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(DetailedIncidentActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", ((dialog, which) -> dialog.dismiss()))
                .show();
    }

    public void navigateToMainScreen() {
        Intent intent = new Intent(DetailedIncidentActivity.this, MainScreenActivity.class);
        startActivity(intent);
        //finish();
    }
}