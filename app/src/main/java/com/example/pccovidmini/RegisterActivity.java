package com.example.pccovidmini;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registerUser;
    private ImageView Backbtn;
    private EditText editName;
    private EditText editDateOfBirth;
    private EditText editSocialInsurance;
    private EditText editNationalID;
    private EditText editAddress;
    private EditText editPhoneNumber;
    private EditText editNumberVaccine;
    private EditText editEmail;
    private EditText editPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);
        Backbtn = (ImageView) findViewById(R.id.ic_back);
        Backbtn.setOnClickListener(this);

        editName = (EditText) findViewById(R.id.fullName);
        editNationalID = (EditText) findViewById(R.id.nationalID);
        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        editDateOfBirth = (EditText) findViewById(R.id.dateOfBirth);
        editSocialInsurance = (EditText) findViewById(R.id.social_insurance);
        editAddress = (EditText) findViewById(R.id.address);
        editPhoneNumber = (EditText) findViewById(R.id.phoneNum);
        editNumberVaccine = (EditText) findViewById(R.id.numberVaccine);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.registerUser:
                registerUser();
                break;
            case R.id.ic_back:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void registerUser() {
        String fullName = editName.getText().toString().trim();
        String dateOfBirth = editDateOfBirth.getText().toString().trim();
        String socialInsurance = editSocialInsurance.getText().toString().trim();
        String nationalID = editNationalID.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String phoneNum = editPhoneNumber.getText().toString().trim();
        String numberVaccine = editNumberVaccine.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password= editPassword.getText().toString().trim();


        if (TextUtils.isEmpty(fullName)) {
            editName.requestFocus();
            editName.setError("This field is required");
            return;
        }
        if (TextUtils.isEmpty(dateOfBirth)) {
            editDateOfBirth.requestFocus();
            editDateOfBirth.setError("This field is required");
            return;
        }
        if (TextUtils.isEmpty(socialInsurance)) {
            editSocialInsurance.requestFocus();
            editSocialInsurance.setError("This field is required");
            return;
        }
        if (socialInsurance.length() != 10){
            editSocialInsurance.setError("SocialInsurance length should be 100 characters!");
            editSocialInsurance.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(nationalID)) {
            editNationalID.requestFocus();
            editNationalID.setError("This field is required");
            return;
        }
        if (nationalID.length()!=10){
            editNationalID.setError("National ID length should be 10 characters!");
            editNationalID.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            editAddress.requestFocus();
            editAddress.setError("This field is required");
            return;
        }

        if (TextUtils.isEmpty(phoneNum)) {
            editPhoneNumber.requestFocus();
            editPhoneNumber.setError("This field is required");
            return;
        }
        if (phoneNum.length()!=10){
            editPhoneNumber.setError("Phone Number length should be 10 characters!");
            editPhoneNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(numberVaccine)) {
            editPhoneNumber.requestFocus();
            editPhoneNumber.setError("This field is required");
            return;
        }
        if (numberVaccine.length()!=1){
            editPhoneNumber.setError("Number of Vaccine length should be 1 characters!");
            editPhoneNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editEmail.requestFocus();
            editEmail.setError("This field is required");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Please provide valid email!");
            editEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            editPassword.setError("This field is required");
            editPassword.requestFocus();
            return;
        }
        if (password.length()<=7){
            editPassword.setError("Min password length should be more than 7 characters!");
            editPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fullName, email,phoneNum, address, dateOfBirth, nationalID,socialInsurance, numberVaccine, password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                            } else
                                                Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}
