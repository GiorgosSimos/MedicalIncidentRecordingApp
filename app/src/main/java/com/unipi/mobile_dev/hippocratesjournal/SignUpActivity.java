package com.unipi.mobile_dev.hippocratesjournal;

import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.editTextEmailSignUp);
        password = findViewById(R.id.editTextPassword2);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void signUp(View view){
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        boolean emailEmpty = userEmail.isEmpty();
        boolean passwordEmpty = userPassword.isEmpty();

        if (!emailEmpty && !passwordEmpty) {// Credentials are both set
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                showMessage(getString(R.string.success_title),getString(R.string.success_signup_description));
                                user = mAuth.getCurrentUser();
                                finish();
                            } else {
                                showMessage(getString(R.string.error_title), task.getException().getLocalizedMessage());
                            }
                        }
                    });
        } else {
            showErrorMessages(emailEmpty, passwordEmpty);
        }
    }

    public void goBack(View view){
        Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
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