package com.unipi.mobile_dev.hippocratesjournal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainScreenActivity extends AppCompatActivity {

    TextView bulletTextView;

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
}