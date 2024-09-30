package com.unipi.mobile_dev.hippocratesjournal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    String searchCriteria = null;
    Spinner searchCriteriaSpinner;
    private EditText searchNameEditText, searchDoeEditText, searchDiagnosisEditText;
    private Button searchButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("incidents");
        searchNameEditText = findViewById(R.id.searchNameEditText);
        searchNameEditText.setVisibility(View.INVISIBLE);
        searchDoeEditText = findViewById(R.id.searchDoeEditText);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        searchDoeEditText.setText(currentDate);
        searchDoeEditText.setOnClickListener(v -> showDatePickerDialog(searchDoeEditText));
        searchDoeEditText.setVisibility(View.INVISIBLE);
        searchDiagnosisEditText = findViewById(R.id.searchDiagnosisEditText);
        searchDiagnosisEditText.setVisibility(View.INVISIBLE);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setVisibility(View.INVISIBLE);
        searchCriteriaSpinner = findViewById(R.id.searchCriteriaSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_criteria_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchCriteriaSpinner.setAdapter(adapter);
        searchCriteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchCriteria = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void selectCriteria(View view) {
        switch (searchCriteria) {
            case "Name":
                searchDoeEditText.setVisibility(View.INVISIBLE);
                searchDiagnosisEditText.setVisibility(View.INVISIBLE);
                searchNameEditText.setVisibility(View.VISIBLE);
                break;
            case "Date of Examination":
                searchNameEditText.setVisibility(View.INVISIBLE);
                searchDiagnosisEditText.setVisibility(View.INVISIBLE);
                searchDoeEditText.setVisibility(View.VISIBLE);
                break;
            case "Diagnosis":
                searchNameEditText.setVisibility(View.INVISIBLE);
                searchDoeEditText.setVisibility(View.INVISIBLE);
                searchDiagnosisEditText.setVisibility(View.VISIBLE);
                break;
        }
        searchButton.setVisibility(View.VISIBLE);
    }

    public void executeSearch(View view) {
        String child = "", textViewValue = "";
        if (searchCriteria == null) {
            showAlert("Warning", "Please define search criteria to continue");
        } else {
            switch (searchCriteria) {
                case "Name":
                    child = "name";
                    textViewValue = searchNameEditText.getText().toString().trim();
                    break;
                case "Date of Examination":
                    child = "dateOfExamination";
                    textViewValue = searchDoeEditText.getText().toString().trim();
                    break;
                case "Diagnosis":
                    child = "diagnosis";
                    textViewValue = searchDiagnosisEditText.getText().toString().trim();
                    break;
            }

            if (!textViewValue.isEmpty()) {
                Query query = reference;
                query = query.orderByChild(child).equalTo(textViewValue);
                // Execute the query
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            displayResults(snapshot);
                        } else {
                            showAlert("Error", "No records found!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showAlert("Search Failed", error.getMessage());
                    }
                });
            } else {
                showAlert("Warning", "Fill in the selected field");
            }
        }
    }

    private void displayResults (DataSnapshot dataSnapshot) {
        // You can extract and display search results here
        StringBuilder results = new StringBuilder();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Incident incident = snapshot.getValue(Incident.class);
            if (incident != null) {
                results.append("Name: ").append(incident.getName()).append("\n")
                        .append("Date of Examination: ").append(incident.getDateOfExamination()).append("\n")
                        .append("Gender: ").append(incident.getGender()).append("\n")
                        .append("Symptoms: ").append(incident.getSymptoms()).append("\n")
                        .append("Diagnosis: ").append(incident.getDiagnosis()).append("\n")
                        .append("Prescription: ").append(incident.getPrescription()).append("\n");
                showAlert("Search Successful", results.toString());
            }
        }
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(SearchActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", ((dialog, which) -> dialog.dismiss()))
                .show();
    }

    private void showDatePickerDialog(final EditText editText){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog and set the current date as default
        DatePickerDialog datePickerDialog = new DatePickerDialog(SearchActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int Year, int Month, int Day) {
                        // Display selected date in the specified EditText (month is 0-indexed)
                        editText.setText(Day + "/" + (Month + 1) + "/" + Year);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
}