package com.example.pccovidmini;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText emailEditText;
    private ImageView Backbtn;
    private Button resetpasswordButton;
    private ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailEditText = (EditText) findViewById(R.id.email);
        resetpasswordButton = (Button) findViewById(R.id.resetpassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        auth    =   FirebaseAuth.getInstance();
        resetpasswordButton.setOnClickListener(this);
        Backbtn = (ImageView) findViewById(R.id.ic_back);
        Backbtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.resetpassword:
                Resetpass();
                break;
            case R.id.ic_back:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void Resetpass(){
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Task<Void> voidTask = auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ForgotPassword.this, "Check your email to reset your password!", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(ForgotPassword.this, "Try again! Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
        startActivity(new Intent(this, LoginActivity.class));
    }
}