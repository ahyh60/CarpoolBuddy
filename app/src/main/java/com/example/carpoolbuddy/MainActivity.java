package com.example.carpoolbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * This activity allows users to navigate to other activities and view their preexisting booked/listed rides
 * @author Amanda Ho
 * @version 0.1
 */
public class MainActivity extends AppCompatActivity  {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    TextView textView1;



    TextView textView2, rideList, rideBook;
    FirebaseUser user;
    Button lrbutton;




    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference carRef = firestore.collection("cars");



    Button brbutton, cancelList, cancelBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        button = findViewById(R.id.logout);
        cancelList = findViewById(R.id.cancelList);
        cancelBook = findViewById(R.id.cancelBook);
        rideList = findViewById(R.id.ListedRide);
        rideBook = findViewById(R.id.BookedRide);
        textView = findViewById(R.id.user_details);
        textView1 = findViewById(R.id.user_name);
        textView2 = findViewById(R.id.user_role);
        user = auth.getCurrentUser();
        lrbutton = findViewById(R.id.list_ride);
        brbutton = findViewById(R.id.book_ride);
        Intent receive = getIntent();

       //Checks if any intent was received from other activities
        if(receive == null){
            rideBook.setVisibility(View.GONE);
            cancelBook.setVisibility(View.GONE);
        }

        //If intent was received from BookRide class, display information about booked ride
        if(receive.getStringExtra("chosenPlate") != null&&receive.getStringExtra("chosenArea") != null){
            String carplate = receive.getStringExtra("chosenPlate");
            String pickup = receive.getStringExtra("chosenArea");
            rideBook.setVisibility(View.VISIBLE);
            rideBook.setText("Booked Ride:" + "\nCar Plate: "+ carplate + "\nPickUp Location: " + pickup);
            cancelBook.setVisibility(View.VISIBLE);

        }
        //If intent was received from listRide class, display information about listed ride
        if(receive.getStringExtra("listedPlate") != null&&receive.getStringExtra("listedArea") != null) {
            String plate = receive.getStringExtra("listedPlate");
            String loc = receive.getStringExtra("listedArea");
            rideList.setVisibility(View.VISIBLE);
            rideList.setText("Listed Ride:" + "\nCar Plate: " + plate + "\nPickUp Location: " + loc);
            cancelList.setVisibility(View.VISIBLE);


            cancelList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rideList.setVisibility(View.GONE);
                    cancelList.setVisibility(View.GONE);
                    carRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        //deletes vehicle object from database
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                Vehicle car = documentSnapshot.toObject(Vehicle.class);
                                String p = car.getVehiclePlate();
                                if(p.equals(plate)){
                                    carRef.document(documentSnapshot.getReference().getId()).delete();
                                }
                            }
                        }
                    });

                }
            });
        }


        cancelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rideBook.setVisibility(View.GONE);
                cancelBook.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Ride cancelled.", Toast.LENGTH_SHORT);
            }
        });


        //Checks if rides have been listed, if not set button visibility gone
        if(rideList.getText().toString().equals("")){
            cancelList.setVisibility(View.GONE);
        }
        //Checks if rides have been booked, if not set button visibility gone
        if(rideBook.getText().toString().equals("")){
            cancelBook.setVisibility(View.GONE);
        }

        //checks if user is null, if so, return to login page
        if(user == null){
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }
        else{
            textView.setText(user.getEmail() + "\n");

        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        lrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListRide.class);
                startActivity(intent);
                finish();
            }
        });


        brbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), BookRide.class);
                startActivity(intent);
                finish();
            }

        });
    }



}