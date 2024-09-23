package com.unipi.mobile_dev.hippocratesjournal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainScreenActivity extends AppCompatActivity {

    TextView bulletTextView;
    //FirebaseDatabase database;
    //DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
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

}