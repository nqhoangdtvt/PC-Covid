package com.example.pccovidmini.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pccovidmini.MainActivity;
import com.example.pccovidmini.R;
import com.example.pccovidmini.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditFragment extends Fragment {
    private View mView;
    private TextView btn_save;
    private EditText etName, etDateOfBirth, etAddress, etPhoneNum, etNumOfVaccine;
    private String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    DatabaseReference reference;
    String fullName, DoB, address, phoneNum, numOfVaccine;
    User userProfile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_edit, container, false);

        initUi();
        setUserInformation();
        initListener();

        return mView;
    }
    private void initUi() {
        btn_save = mView.findViewById(R.id.tv_save);
        etName = mView.findViewById(R.id.et_name);
        etDateOfBirth = mView.findViewById(R.id.et_dateOfBirth);
        etAddress = mView.findViewById(R.id.et_address);
        etPhoneNum = mView.findViewById(R.id.et_phoneNum);
        etNumOfVaccine = mView.findViewById(R.id.et_numberVaccine);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users");
    }

    private void setUserInformation() {
        if(user == null){
            return;
        }
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    showInfoUser(userProfile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showInfoUser(User userProfile){
        fullName = userProfile.fullName;
        DoB = userProfile.dateOfBirth;
        address = userProfile.address;
        phoneNum = userProfile.phoneNum;
        numOfVaccine = userProfile.numOfVaccine;

        etName.setText(fullName);
        etDateOfBirth.setText(DoB);
        etAddress.setText(address);
        etPhoneNum.setText(phoneNum);
        etNumOfVaccine.setText(numOfVaccine);
    }

    private void initListener() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });
    }

    private void onClickUpdateProfile() {
        boolean updated = false;
        if(isNameChange()){
            updated = true;
        }
        if(isAddressChange()){
            updated = true;
        }
        if(isDoBChange()){
            updated = true;
        }
        if(isPhoneNumChange()){
            updated = true;
        }
        if(isNumVaccineChange()){
            updated = true;
        }
        if(updated){
            Toast.makeText(getActivity(), "Data has been updated", Toast.LENGTH_SHORT).show();
//            mainActivity.showInfoUser(userProfile);
            startActivity(new Intent(getActivity(), MainActivity.class));
        }else{
            Toast.makeText(getActivity(), "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
        }
        if(etName.getText().toString().isEmpty() ||
                etDateOfBirth.getText().toString().isEmpty() ||
                etAddress.getText().toString().isEmpty() ||
                etPhoneNum.getText().toString().isEmpty() ||
                etNumOfVaccine.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "One or many fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private boolean isNumVaccineChange() {
        if(!numOfVaccine.equals(etNumOfVaccine.getText().toString())){
            reference.child(userID).child("numOfVaccine").setValue(etNumOfVaccine.getText().toString());
            numOfVaccine = etNumOfVaccine.getText().toString();
            return true;
        }else{
            return false;
        }
    };

    private boolean isPhoneNumChange() {
        if(!phoneNum.equals(etPhoneNum.getText().toString())){
            reference.child(userID).child("phoneNum").setValue(etPhoneNum.getText().toString());
            phoneNum = etPhoneNum.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean isAddressChange() {
        if(!address.equals(etAddress.getText().toString())){
            reference.child(userID).child("address").setValue(etAddress.getText().toString());
            address = etAddress.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean isDoBChange() {
        if(!DoB.equals(etDateOfBirth.getText().toString())){
            reference.child(userID).child("dateOfBirth").setValue(etDateOfBirth.getText().toString());
            DoB = etDateOfBirth.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean isNameChange() {
        if(!fullName.equals(etName.getText().toString())){
            reference.child(userID).child("fullName").setValue(etName.getText().toString());
            fullName = etName.getText().toString();
            return true;
        }else{
            return false;
        }
    }
}
