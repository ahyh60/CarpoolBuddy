package com.example.carpoolbuddy;

import android.os.Parcelable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Class template for the vehicle object
 * @author Amanda Ho
 * @version 0.1
 */
public class Vehicle {
    private String vehiclePlate;
    private String capacity;
    private String district;
    private String area;

    private User owner;



    //firestore needs empty constructor
    public Vehicle(){}

    //constructor
    public Vehicle(String v, String c, String d, String a, User o){
        vehiclePlate = v;
        capacity = c;
        district = d;
        area = a;
        owner = o;
    }

    //accessors and mutators

    public String getCapacity() {
        return capacity;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public String getDistrict() {
        return district;
    }

    public String getArea() {
        return area;
    }
    public User getUser(){
        return owner;
    }

    public void setCapacity(String c){
        capacity = c;
    }
    public void setVehiclePlate(String p){
        vehiclePlate = p;
    }

    public void setDistrict(String d){
        district = d;
    }

    public void setArea(String a){
        area = a;
    }

    public void setOwner(User o){
        owner = o;
    }


}





