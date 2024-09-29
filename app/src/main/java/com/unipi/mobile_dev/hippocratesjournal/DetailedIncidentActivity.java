package com.unipi.mobile_dev.hippocratesjournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailedIncidentActivity extends AppCompatActivity {

    TextView textViewName,
            textViewDob,
            textViewDoe,
            textViewGender,
            textViewSymptoms,
            textViewDiagnosis,
            textViewPrescription;
    FirebaseDatabase database;
    DatabaseReference reference;
    String incidentId;
    Button updateButton, deleteButton;
    private boolean isEditMode = false;// Variable used to determine if we're in view or edit mode

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

        updateButton = findViewById(R.id.updateIncidentButton);
        deleteButton = findViewById(R.id.deleteIncidentButton);
        setFieldsEditable(false);

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

    public void updateIncident(View view) {
        if (!isEditMode) { // If we're in view mode, switch to edit mode
            Toast.makeText(DetailedIncidentActivity.this, "Please update the desired information", Toast.LENGTH_SHORT).show();
            isEditMode = true;
            updateButton.setText(R.string.dtld_save_button); // Change button text to "Save"
            deleteButton.setEnabled(false);// Delete button is disabled while the update is underway
            clearFields();
            setFieldsEditable(true);      // Enable editing of text fields
        } else { // If we're in edit mode, show the confirmation dialog
            showUpdateConfirmationDialog();
        }
    }

    private void clearFields() {
        textViewSymptoms.setText("");
        textViewDiagnosis.setText("");
        textViewPrescription.setText("");

        textViewSymptoms.setHint("Type in symptoms");
        textViewDiagnosis.setHint("Type in diagnosis");
        textViewPrescription.setHint("Type in prescription");
    }

    private void updateIncidentInDB() {
        String updatedSymptoms = textViewSymptoms.getText().toString();
        String updatedDiagnosis = textViewDiagnosis.getText().toString();
        String updatedPrescription = textViewPrescription.getText().toString();
        boolean symptomsEmpty = updatedSymptoms.isEmpty();
        boolean diagnosisEmpty = updatedDiagnosis.isEmpty();
        boolean prescriptionEmpty = updatedPrescription.isEmpty();

        if (!symptomsEmpty && !diagnosisEmpty && !prescriptionEmpty) {
            reference.child(incidentId)
                    .child("symptoms").setValue(updatedSymptoms);
            reference.child(incidentId)
                    .child("diagnosis").setValue(updatedDiagnosis);
            reference.child(incidentId)
                    .child("prescription").setValue(updatedPrescription)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showAlert("Successful Update", "Incident updated successfully");
                            updateButton.setText(R.string.dtld_update_button);
                            deleteButton.setEnabled(true);
                            //set fields to view mode (non-editable)
                            isEditMode = false;
                            setFieldsEditable(false);
                        } else {
                            showAlert("Update Failed", "An unexpected error occurred, please try again!");
                        }
                    });
        } else {
            showErrorMessages(symptomsEmpty, diagnosisEmpty, prescriptionEmpty);
        }

    }

    private void setFieldsEditable(boolean editable) {
        textViewSymptoms.setFocusable(editable);
        textViewSymptoms.setFocusableInTouchMode(editable);
        textViewSymptoms.setCursorVisible(editable);
        textViewSymptoms.setClickable(editable);

        textViewDiagnosis.setFocusable(editable);
        textViewDiagnosis.setFocusableInTouchMode(editable);
        textViewDiagnosis.setCursorVisible(editable);
        textViewDiagnosis.setClickable(editable);

        textViewPrescription.setFocusable(editable);
        textViewPrescription.setFocusableInTouchMode(editable);
        textViewPrescription.setCursorVisible(editable);
        textViewPrescription.setClickable(editable);
    }

    private void showUpdateConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Update");
        builder.setMessage("Are you sure you want to save these changes?");
        builder.setPositiveButton("Confirm", (dialog, which) -> {// Save updated incident to Firebase
            updateIncidentInDB();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Delete the incident from Firebase using incidentId
    public void deleteIncident(View view) {
        // Create an AlertDialog to ask for confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedIncidentActivity.this);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Are you sure you want to delete this incident");

        // If the user clicks Yes, delete the incident
        builder.setPositiveButton("Yes", ((dialog, which) -> reference.child(incidentId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showAlert("Successful Delete","Incident deleted successfully!");
                    } else {
                        showAlert("Delete Failed","Failed to delete incident. Please try again.");
                    }
                    navigateToMainScreen();
                })));

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
    }

    private void showErrorMessages(boolean symptomsEmpty, boolean diagnosisEmpty, boolean prescriptionEmpty) {
        String errorMessage;
        if (symptomsEmpty && diagnosisEmpty && prescriptionEmpty){
            errorMessage = "Symptoms, Diagnosis and Prescription cannot be empty";
        } else if (symptomsEmpty && diagnosisEmpty) {
            errorMessage = "Symptoms and Diagnosis cannot be empty";
        } else if (symptomsEmpty && prescriptionEmpty) {
            errorMessage = "Symptoms and Prescription cannot be empty";
        } else if (diagnosisEmpty && prescriptionEmpty) {
            errorMessage = "Diagnosis and Prescription cannot be empty";
        } else if (symptomsEmpty) {
            errorMessage = "Symptoms cannot be empty";
        } else if (diagnosisEmpty) {
            errorMessage = "Diagnosis cannot be empty";
        } else {
            errorMessage = "Prescription cannot be empty";
        }
        showMessage("Error", errorMessage);
    }

    void showMessage(String title, String message){
        new AlertDialog.Builder(this).
                setTitle(title).
                setMessage(message).
                setCancelable(true).
                show();
    }
}