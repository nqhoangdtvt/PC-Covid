package com.example.pccovidmini.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pccovidmini.MainActivity;
import com.example.pccovidmini.QrActivity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ManagementFragment extends Fragment {
    MainActivity mainActivity = (MainActivity) getActivity();

    private View mView;
    private ImageView imgAvatar;
    private TextView tvName, tvSocialInsurance, tvDateOfBirth,
            tvNationalID, tvPhoneNum, tvAddress, tvNumOfVaccine;
    private String userID;
    private Button btn_qr;

    DatabaseReference reference;
    StorageReference storageReference;
    StorageReference profileRef;

    User userProfile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_personal_management, container, false);
        initUi();
        setUserInformation();
        initListener();
        return mView;
    }

    private void initUi(){
        btn_qr = mView.findViewById(R.id.btn_qr);
        tvName = mView.findViewById(R.id.tv_name_management);
        tvSocialInsurance = mView.findViewById(R.id.tv_bhyt_management);
        tvDateOfBirth = mView.findViewById(R.id.tv_dob_management);
        tvNationalID = mView.findViewById(R.id.tv_cccd_management);
        tvPhoneNum = mView.findViewById(R.id.tv_phone_management);
        tvAddress = mView.findViewById(R.id.tv_address_management);
        tvNumOfVaccine = mView.findViewById(R.id.tv_numOfVaccine_management);
        imgAvatar = mView.findViewById(R.id.img_ava);
        storageReference = FirebaseStorage.getInstance().getReference();
        profileRef = storageReference.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
    }

    public void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    showInfoUser(userProfile);
//                    Glide.with(getActivity()).load(photoUrl).error(R.drawable.ic_baseline_person_24).into(imgAvatar);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showInfoUser(User userProfile){
        String fullName = userProfile.fullName;
        String socialInsurance = userProfile.socialInsurance;
        String DoB = userProfile.dateOfBirth;
        String NationalID = userProfile.nationalID;
        String phoneNum = userProfile.phoneNum;
        String address = userProfile.address;
        String numOfVaccine = userProfile.numOfVaccine;

        tvName.setText(fullName);
        tvSocialInsurance.setText(socialInsurance);
        tvDateOfBirth.setText(DoB);
        tvNationalID.setText(NationalID);
        tvPhoneNum.setText(phoneNum);
        tvAddress.setText(address);
        tvNumOfVaccine.setText(numOfVaccine);

//      setAvatar
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgAvatar);
            }
        });
    }



//    click vao anh
    private void initListener() {
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });


        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQR();
            }
        });

    }



    private void onClickRequestPermission() {
//        open gallery
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
//                imgAvatar.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imgAvatar);
                    }
                });
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();     
            }
        });
    }

    private void createQR() {
        Intent intent = new Intent(getActivity(), QrActivity.class);
        Bundle bundle = new Bundle();
        String data ="name: "+ tvName.getText().toString()+ ", SocialInsurance: " + tvSocialInsurance.getText().toString()+ ", NationalID: " +
                tvNationalID.getText().toString() + ", NumOfVaccine: " + tvNumOfVaccine.getText().toString();
        bundle.putString("data", data);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
