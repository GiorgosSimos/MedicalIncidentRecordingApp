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

import java.util.Locale;

public class MainScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Locale locale = Locale.getDefault();
    String language = locale.getLanguage();
    TextView bulletTextView;
    String userType, bulletText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.unipi.mobile_dev.hippocratesjournal",
                Context.MODE_PRIVATE);
        userType = sharedPreferences.getString("UserType", "");
        bulletTextView = findViewById(R.id.bulletTextView);
        if (language.equals("es")) {
            bulletText = "<p>La aplicación te permite realizar las siguientes tareas:</p>" +
                    "<br>" +
                    "<ul>" +
                    "<li>Registrar un nuevo incidente</li>" +
                    "<li>Buscar un incidente</li>" +
                    "<li>Mostrar todos los incidentes</li>" +
                    "</ul>";
        } else {
            bulletText = "<p>The app allows you to perform the following tasks:</p>" +
                    "<br>" +
                    "<ul>" +
                    "<li>Record a new incident </li>" +
                    "<li>Search an incident </li>" +
                    "<li>Display All incidents </li>" +
                    "</ul>";
        }

        bulletTextView.setText(Html.fromHtml(bulletText, Html.FROM_HTML_MODE_COMPACT));
    }

    public void goNewIncident(View view) {
        if (!userType.equals("Visitor")) {
            Intent intent = new Intent(MainScreenActivity.this, NewIncidentActivity.class);
            startActivity(intent);
            finish();
        } else {
            showAlert(getString(R.string.new_incident), getString(R.string.user_restriction));
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