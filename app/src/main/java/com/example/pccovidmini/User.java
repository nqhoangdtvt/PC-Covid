package com.example.pccovidmini;

import java.sql.Date;

public class User {
    public String fullName, email, address,phoneNum, dateOfBirth,
            nationalID, socialInsurance, numOfVaccine, passWord;
    public User() {
    }
    public User(String fullName, String email, String phoneNum, String address, String dateOfBirth, String nationalID, String socialInsurance, String numOfVaccine, String passWord){
        this.fullName = fullName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.nationalID = nationalID;
        this.socialInsurance = socialInsurance;
        this.numOfVaccine = numOfVaccine;
        this.passWord=passWord;
    }

    public User(String fullName, String phoneNum, String address, String dateOfBirth, String numOfVaccine){
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.numOfVaccine = numOfVaccine;
    }
}
