package com.example.ayfalakh.midmobileproject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserModel extends RealmObject {
    @Required
    @PrimaryKey
    private String phone;
    @Required
    private String name;
    @Required
    private String pass;
    private boolean isMale;
    private String address;
    private String birthdate;


    public UserModel() {
    }

    public UserModel(String phone, String name, String pass, boolean isMale, String address, String birthdate) {
        this.phone = phone;
        this.name = name;
        this.pass = pass;
        this.isMale = isMale;
        this.address = address;
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
}