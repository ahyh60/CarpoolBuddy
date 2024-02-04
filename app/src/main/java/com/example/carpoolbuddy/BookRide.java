package com.example.carpoolbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Iterator;

public class BookRide extends AppCompatActivity {


    FirebaseAuth auth;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference carRef = firestore.collection("cars");

    Spinner spinner;

    TextView ride1, ride2, ride3;

    Button option1, option2, option3;

    Vehicle car1, car2, car3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ride);
        auth = FirebaseAuth.getInstance();
        ride1 = findViewById(R.id.ride1);
        ride2 = findViewById(R.id.ride2);
        ride3 = findViewById(R.id.ride3);
        option1 = findViewById(R.id.Option1);
        option2 = findViewById(R.id.Option2);
        option3 = findViewById(R.id.Option3);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //Checks what district the user has selected in the spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choose = parent.getItemAtPosition(position).toString();
                Toast.makeText(BookRide.this, "Selected Item:" + choose, Toast.LENGTH_SHORT).show();
                carRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    //Iterates through collection to check which cars have the same district, adds those vehicle objects to arraylist
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Vehicle> nearCars = new ArrayList<>();
                        nearCars.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Vehicle car = documentSnapshot.toObject(Vehicle.class);
                            String d = car.getDistrict();
                            if (d.equals(parent.getItemAtPosition(position).toString())){
                                nearCars.add(car);
                            }
                        }

                        //Checks if arraylist has next element, displays first 3 vehicle information in textviews

                        Iterator<Vehicle> iterator = nearCars.iterator();
                        if (iterator.hasNext()) {
                            car1 = iterator.next();
                            ride1.setText(car1.getVehiclePlate() + "\n" + car1.getArea());
                            if (iterator.hasNext()) {
                                car2 = iterator.next();
                                ride2.setText(car2.getVehiclePlate() + "\n" + car2.getArea());
                                if (iterator.hasNext()) {
                                    car3 = iterator.next();
                                    ride3.setText(car3.getVehiclePlate()+"\n" + car3.getArea());
                                }
                            }
                        }

                        //Sends intent extra( Vehicle information to CarDetails activity) depending on which of the 3 buttons the user presses
                        option1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(BookRide.this, CarDetails.class);
                                intent.putExtra("plate", car1.getVehiclePlate());
                                intent.putExtra("capacity", car1.getCapacity());
                                intent.putExtra("district", car1.getDistrict());
                                intent.putExtra("area", car1.getArea());
                                startActivity(intent);
                                finish();
                            }
                        });

                        option2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(BookRide.this, CarDetails.class);
                                intent.putExtra("plate", car2.getVehiclePlate());
                                intent.putExtra("capacity", car2.getCapacity());
                                intent.putExtra("district", car2.getDistrict());
                                intent.putExtra("area", car2.getArea());
                                startActivity(intent);
                                finish();
                            }
                        });

                        option3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(BookRide.this, CarDetails.class);
                                intent.putExtra("plate", car3.getVehiclePlate());
                                intent.putExtra("capacity", car3.getCapacity());
                                intent.putExtra("district", car3.getDistrict());
                                intent.putExtra("area", car3.getArea());
                                startActivity(intent);
                                finish();
                            }
                        });


                    }
                });


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


    }


    }
