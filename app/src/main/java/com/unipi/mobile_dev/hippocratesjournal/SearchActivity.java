package com.unipi.mobile_dev.hippocratesjournal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    String searchCriteria = null;
    Spinner searchCriteriaSpinner;
    private EditText searchNameEditText, searchDoeEditText, searchDiagnosisEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
                //Start the SearchResultsActivity with the selected searchCriteria and filled text value
                goSearchResults(child, textViewValue);
            } else {
                showAlert("Warning", "Fill in the selected field");
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

    private void goSearchResults(String criteria, String filledTextValue) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("searchCriteria", criteria);
        intent.putExtra("searchValue", filledTextValue);
        startActivity(intent);
    }

    public void backPressed(View view) {
        Intent intent = new Intent(SearchActivity.this, MainScreenActivity.class);
        startActivity(intent);
        finish();
    }
}