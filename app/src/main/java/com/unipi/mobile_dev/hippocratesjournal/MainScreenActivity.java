package com.unipi.mobile_dev.hippocratesjournal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

//import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainScreenActivity extends AppCompatActivity {

    TextView bulletTextView;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        database = FirebaseDatabase.getInstance();
        bulletTextView = findViewById(R.id.bulletTextView);
        String bulletText = "<p>The app allows you to perform the following tasks:</p>" +
                "<br>" +
                "<ul>" +
                "<li>Record a new incident </li>" +
                "<li>Search an incident </li>" +
                "<li>Display All incidents </li>" +
                "</ul>";
        bulletTextView.setText(Html.fromHtml(bulletText, Html.FROM_HTML_MODE_COMPACT));
    }

    public void goNewIncident(View view) {
        Intent intent = new Intent(MainScreenActivity.this, NewIncidentActivity.class);
        startActivity(intent);
    }

    public void displayAll(View view) {
        reference = database.getReference("incidents");

        // Retrieve all incidents from the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder incidentsStringBuilder = new StringBuilder();

                // Iterate through each incident
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Incident incident = snapshot.getValue(Incident.class);
                    //Add the incident details to the StringBuilder
                    incidentsStringBuilder.append("Name: ").append(incident.getName()).append("\n");
                    incidentsStringBuilder.append("Date of Birth: ").append(incident.getDateOfBirth()).append("\n");
                    incidentsStringBuilder.append("Date of Examination: ").append(incident.getDateOfExamination()).append("\n");
                    incidentsStringBuilder.append("Gender: ").append(incident.getGender()).append("\n");
                    incidentsStringBuilder.append("Symptoms: ").append(incident.getSymptoms()).append("\n");
                    incidentsStringBuilder.append("Diagnosis: ").append(incident.getDiagnosis()).append("\n");
                    incidentsStringBuilder.append("Prescription: ").append(incident.getPrescription()).append("\n");
                    incidentsStringBuilder.append("--------------------------------------------------------------\n");
                }

                //Display the incidents in an AlertDialog
                showIncidentDialog(incidentsStringBuilder.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayAllIncidents(View view) {
        Intent intent = new Intent(MainScreenActivity.this, DisplayAllIncidentsActivity.class);
        startActivity(intent);
    }

    void showMessage(String title, String message){
        new AlertDialog.Builder(this).
                setTitle(title).
                setMessage(message).
                setCancelable(true).
                show();
    }

    private void showIncidentDialog(String incidents) {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("All incidents");
        builder.setMessage(incidents);

        // Set a button to close the dialog
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        //Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}