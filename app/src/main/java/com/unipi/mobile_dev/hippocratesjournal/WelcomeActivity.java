package com.unipi.mobile_dev.hippocratesjournal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText email, password;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharedPreferences = getSharedPreferences("com.unipi.mobile_dev.hippocratesjournal",
                Context.MODE_PRIVATE);
        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void goSignIn(View view){
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        boolean emailEmpty = userEmail.isEmpty();
        boolean passwordEmpty = userPassword.isEmpty();

        if (!emailEmpty && !passwordEmpty){// Credentials are both set
            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){// Log In Successful
                                showMessage(getString(R.string.success_title), getString(R.string.success_signin_description));
                                saveUserType(userEmail);
                                navigateToMainScreen();
                            } else {
                                showMessage(getString(R.string.error_title),task.getException().getLocalizedMessage());
                            }
                        }
                    });
        } else {
            showErrorMessages(emailEmpty, passwordEmpty);
        }
    }

    public void goSignUp(View view){
        Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void saveUserType(String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserType", userEmail);
        editor.apply();
    }

    public void visitor(View view){
        saveUserType("Visitor");
        navigateToMainScreen();
    }

    private void navigateToMainScreen(){
        Intent intent = new Intent(WelcomeActivity.this, MainScreenActivity.class);
        startActivity(intent);
    }

    private void showErrorMessages(boolean emailEmpty, boolean passwordEmpty) {
        String errorMessage;
        if (emailEmpty && passwordEmpty) {
            errorMessage = getString(R.string.error_email_password_empty);
        } else if (emailEmpty) {
            errorMessage = getString(R.string.error_email_empty);
        } else {
            errorMessage = getString(R.string.error_password_empty);
        }
        showMessage(getString(R.string.error_title), errorMessage);
    }

    void showMessage(String title, String message){
        new AlertDialog.Builder(this).
                setTitle(title).
                setMessage(message).
                setCancelable(true).
                show();
    }
}