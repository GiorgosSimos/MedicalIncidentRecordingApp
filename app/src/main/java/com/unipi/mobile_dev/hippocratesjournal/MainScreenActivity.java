package com.unipi.mobile_dev.hippocratesjournal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView bulletTextView;
    String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.unipi.mobile_dev.hippocratesjournal",
                Context.MODE_PRIVATE);
        userType = sharedPreferences.getString("UserType", "");
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
        if (!userType.equals("Visitor")) {
            Intent intent = new Intent(MainScreenActivity.this, NewIncidentActivity.class);
            startActivity(intent);
            finish();
        } else {
            showAlert("New Incident", "Available only for logged in users");
        }
    }

    public void searchIncident(View view) {
        Intent intent = new Intent(MainScreenActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void displayAllIncidents(View view) {
        Intent intent = new Intent(MainScreenActivity.this, DisplayAllIncidentsActivity.class);
        startActivity(intent);
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(MainScreenActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", ((dialog, which) -> dialog.dismiss()))
                .show();
    }

}