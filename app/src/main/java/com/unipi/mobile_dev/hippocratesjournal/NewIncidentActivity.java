package com.unipi.mobile_dev.hippocratesjournal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewIncidentActivity extends AppCompatActivity {

    Spinner genderSpinner;
    String patientGender;
    EditText name, dobEditText,doeEditText, symptoms, diagnosis, prescription;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incident);
        database = FirebaseDatabase.getInstance();
        name = findViewById(R.id.editTextName);
        symptoms = findViewById(R.id.editTextSymptoms);
        diagnosis = findViewById(R.id.editTextDiagnosis);
        prescription = findViewById(R.id.editTextPrescription);
        genderSpinner = findViewById(R.id.genderSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
                patientGender = parent.getItemAtPosition(position).toString();
                // Do something with the selected gender (e.g., save it, display it)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handle case when nothing is selected
            }
        });
        dobEditText = findViewById(R.id.dobEditText);
        dobEditText.setOnClickListener(v -> showDatePickerDialog(dobEditText));

        doeEditText = findViewById(R.id.doeEditText);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        // Date of Examination is pre-filled to display current date
        doeEditText.setText(currentDate);
        doeEditText.setOnClickListener(v -> showDatePickerDialog(doeEditText));
    }

    private void showDatePickerDialog(final EditText editText){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog and set the current date as default
        DatePickerDialog datePickerDialog = new DatePickerDialog(NewIncidentActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int Year, int Month, int Day) {
                        // Display selected date in the specified EditText (month is 0-indexed)
                        editText.setText(Day + "/" + (Month + 1) + "/" + Year);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    public void registerIncident(View view) {
        String patientName = name.getText().toString();
        String dateOfBirth = dobEditText.getText().toString();
        String dateOfExamination = doeEditText.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();
        String patientSymptoms = symptoms.getText().toString();
        String doctorDiagnosis = diagnosis.getText().toString();
        String doctorPrescription = prescription.getText().toString();
        boolean nameEmpty = patientName.isEmpty();
        boolean symptomsEmpty = patientSymptoms.isEmpty();
        boolean diagnosisEmpty = doctorDiagnosis.isEmpty();
        boolean prescriptionEmpty = doctorPrescription.isEmpty();

        Incident incident = new Incident(
                patientName,
                dateOfBirth,
                dateOfExamination,
                gender,
                patientSymptoms,
                doctorDiagnosis,
                doctorPrescription
        );

        if (!nameEmpty && !symptomsEmpty && !diagnosisEmpty && !prescriptionEmpty) {
            reference = database.getReference("incidents");
            reference.push().setValue(incident).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showMessage(getString(R.string.success_title), getString(R.string.register_success_descr));

                } else {
                    showMessage(getString(R.string.error_title), getString(R.string.error_generic_descr));
                }
                navigateToMainScreen();
            });
        } else {
            showErrorMessages(nameEmpty, symptomsEmpty, diagnosisEmpty, prescriptionEmpty);
        }
    }

    public void navigateToMainScreen() {
        Intent intent = new Intent(NewIncidentActivity.this, MainScreenActivity.class);
        startActivity(intent);
    }

    private void showErrorMessages(boolean nameEmpty, boolean symptomsEmpty, boolean diagnosisEmpty, boolean prescriptionEmpty) {
        StringBuilder errorMessage = new StringBuilder();

        if (nameEmpty) {
            errorMessage.append(getString(R.string.patient_name));
        }
        if (symptomsEmpty) {
            if (errorMessage.length() > 0) errorMessage.append(", ");
            errorMessage.append(getString(R.string.referent_symptoms));
        }
        if (diagnosisEmpty) {
            if (errorMessage.length() > 0) errorMessage.append(", ");
            errorMessage.append(getString(R.string.prescription));
        }
        if (prescriptionEmpty) {
            if (errorMessage.length() > 0) errorMessage.append(", ");
            errorMessage.append(getString(R.string.prescription));
        }

        if (errorMessage.length() > 0) {
            showMessage(getString(R.string.error_title), errorMessage.append(getString(R.string.cannot_empty)).toString());
        }
    }

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", ((dialog, which) -> dialog.dismiss()))
                .show();
    }

    public void backPressed(View view) {
        Intent intent = new Intent(NewIncidentActivity.this, MainScreenActivity.class);
        startActivity(intent);
        finish();
    }
}