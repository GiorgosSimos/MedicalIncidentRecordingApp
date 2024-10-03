package com.unipi.mobile_dev.hippocratesjournal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Locale;

public class MainScreenActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu_icon;
    LinearLayout home, all_incidents, about, contact, login_logout;
    SharedPreferences sharedPreferences;
    Locale locale = Locale.getDefault();
    String language = locale.getLanguage();
    TextView bulletTextView, user_info, login_logout_text;
    String userType, bulletText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.unipi.mobile_dev.hippocratesjournal",
                Context.MODE_PRIVATE);
        userType = sharedPreferences.getString("UserType", "");
        drawerLayout = findViewById(R.id.drawerLayout);
        menu_icon = findViewById(R.id.burger_menu);
        user_info = findViewById(R.id.user_info);
        // Link options of hamburger menu
        home = findViewById(R.id.home);
        all_incidents = findViewById(R.id.all_incidents);
        about = findViewById(R.id.about);
        contact = findViewById(R.id.contact);
        login_logout = findViewById(R.id.login_logout);
        login_logout_text = findViewById(R.id.login_logout_text);
        if (userType.equals("Visitor")) {
            login_logout_text.setText(getString(R.string.login_logout));
        } else {
            login_logout_text.setText(getString(R.string.logout));
        }
        user_info.setText(userType);
        menu_icon.setOnClickListener(v -> openDrawer(drawerLayout));
        home.setOnClickListener(v -> {recreate();});
        all_incidents.setOnClickListener(v -> {redirectActivity(MainScreenActivity.this,
                DisplayAllIncidentsActivity.class);});
        about.setOnClickListener(v -> {redirectActivity(MainScreenActivity.this, AboutActivity.class);});
        contact.setOnClickListener(v -> {redirectActivity(MainScreenActivity.this, ContactActivity.class);});
        login_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userType.equals("Visitor")) {// User is signed in as guest
                        Toast.makeText(MainScreenActivity.this,
                                getString(R.string.login_singup), Toast.LENGTH_SHORT).show();
                } else {// User has signed in with credentials
                        Toast.makeText(MainScreenActivity.this,
                                getString(R.string.logout_successful), Toast.LENGTH_SHORT).show();
                }
                redirectActivity(MainScreenActivity.this, WelcomeActivity.class);
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                prefsEditor.clear();
                prefsEditor.apply();
            }

        });
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

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void goNewIncident(View view) {
        if (!userType.equals("Visitor")) {
            Intent intent = new Intent(MainScreenActivity.this, NewIncidentActivity.class);
            startActivity(intent);
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