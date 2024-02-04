package com.example.carpoolbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This activity allows users to list a ride and add a vehicle to the database
 * @author Amanda Ho
 * @version 0.1
 */
public class ListRide extends AppCompatActivity {

    TextInputEditText editVehiclePlate, capacityAvailable, district;
    Button buttonList;

    FirebaseFirestore firestore;


    FirebaseAuth auth;



    Spinner spinner;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ride);
        auth = FirebaseAuth.getInstance();
        editVehiclePlate = findViewById(R.id.vehiclePlate);
        capacityAvailable = findViewById(R.id.CapacityAvailable);
        district = findViewById(R.id.district);
        buttonList = findViewById(R.id.ListRideButton);
        firestore = FirebaseFirestore.getInstance();
        CollectionReference users = firestore.collection("users");
        CollectionReference cars = firestore.collection("cars");
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(ListRide.this, "Selected Item:" + item, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Central and Western");
        arrayList.add("Eastern");
        arrayList.add("Southern");
        arrayList.add("Wan Chai");
        arrayList.add("Islands");
        arrayList.add("Kwai Tsing");
        arrayList.add("North");
        arrayList.add("Sai Kung");
        arrayList.add("Sha Tin");
        arrayList.add("Tai Po");
        arrayList.add("Tsuen Wan");
        arrayList.add("Tuen Mun");
        arrayList.add("Yuen Long");
        arrayList.add("Kowloon City");
        arrayList.add("Sham Shui Po");
        arrayList.add("Wong Tai Sin");
        arrayList.add("Yau Tsim Mong");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);


        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    String plate = editVehiclePlate.getText().toString();
                    String capacity = capacityAvailable.getText().toString();
                    String item = spinner.getSelectedItem().toString();
                    String location = district.getText().toString();

                    //Finds user by comparing email to emails in database
                    //Adds vehicle to firestore collection

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            User owner = documentSnapshot.toObject(User.class);
                            String email = owner.getEmail();
                            String name = owner.getName();
                            String role = owner.getRole();
                            if(email.equals(auth.getCurrentUser().getEmail().toString())){
                                User carOwner = new User(name, email, role);
                                Vehicle car = new Vehicle(plate, capacity, item, location, carOwner);
                                cars.add(car);
                            }
                        }

                        //Sends listed information as intent extra to main activity class
                        Intent intent = new Intent(ListRide.this, MainActivity.class);
                        intent.putExtra("listedPlate", plate);
                        intent.putExtra("listedArea", capacity);
                        intent.putExtra("listedLocation", location);
                        startActivity(intent);
                        finish();


                    }

                });




            }
        });


    }



}