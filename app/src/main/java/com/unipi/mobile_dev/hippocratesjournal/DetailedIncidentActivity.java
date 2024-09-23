package com.unipi.mobile_dev.hippocratesjournal;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailedIncidentActivity extends AppCompatActivity {

    private TextView textViewName, textViewDob, textViewDoe, textViewGender, textViewSymptoms, textViewDiagnosis, textViewPrescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_incident);

        // Initialize the views
        textViewName = findViewById(R.id.editTextIncidentName);
        textViewDob = findViewById(R.id.incidentDobEditText);
        textViewDoe = findViewById(R.id.incidentDoeEditText);
        textViewGender = findViewById(R.id.incidentGenderEditText);
        textViewSymptoms = findViewById(R.id.editTextIncidentSymptoms);
        textViewDiagnosis = findViewById(R.id.editTextIncidentDiagnosis);
        textViewPrescription = findViewById(R.id.editTextIncidentPrescription);

        // Get data from intent
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
}