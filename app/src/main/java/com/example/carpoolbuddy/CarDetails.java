package com.example.carpoolbuddy;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
/**
 * This activity allows users to access details about a specific car object and view location on a map. It also allows users to book the ride.
 * @author Amanda Ho
 * @version 0.1
 */
public class CarDetails extends AppCompatActivity implements OnMapReadyCallback {
    TextView capacity, area, carPlate, distance;

    private GoogleMap myMap;
    private String search;

    private double km;


    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;

    Button bookButton;
    FusedLocationProviderClient fusedLocationProviderClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        capacity = findViewById(R.id.capacity);
        carPlate = findViewById(R.id.carplate);
        distance = findViewById(R.id.distance);
        area = findViewById(R.id.area);
        bookButton = findViewById(R.id.BookRideButton);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        //Receives intent from book ride class and displays the information in textviews
        Intent intent = getIntent();
        String getCapacity = intent.getStringExtra("capacity");
        String getCarplate = intent.getStringExtra("plate");
        String getArea = intent.getStringExtra("area");
        search = getArea;
        carPlate.setText("Car Plate: " + getCarplate);
        capacity.setText("Capacity:" + getCapacity);
        area.setText("PickUp Address: " + getArea);


        //Sends intent extra back to main activity class if user decides to book vehicle
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarDetails.this, MainActivity.class);
                intent.putExtra("chosenPlate", getCarplate);
                intent.putExtra("chosenArea", getArea);
                startActivity(intent);
                finish();
            }
        });

    }


    //Gets the users last location and calls onMapReady method
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(CarDetails.this);


                }
            }
        });
    }



//    @Override
    //Initializes map, uses lat and long to find location of current location and pickup address
    //Places markers on both those addresses, calculates distance between locations and displays
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(CarDetails.this);
        try{
            addressList = geocoder.getFromLocationName(search, 1);
        }catch(IOException e){
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
        LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        float zoomLevel = 16.0f;
        myMap.addMarker(new MarkerOptions().position(latlng).title("hk"));
        myMap.addMarker(new MarkerOptions().position(current).title("currentLocation"));

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));

        km =  calculateDistance(currentLocation.getLatitude(), currentLocation.getLongitude(), address.getLatitude(), address.getLongitude());
        if(km<1){ //Converts km to m if km is less than 1 (otherwise will say 0 metres when converted to int)
            km = km*1000;
            String d = (int) km + "";
            distance.setText(d + "m away from your location");
        }
        else{
            String d = (int)km + "";
            distance.setText(d + "km away from your location.");}

    }


    //Method used to calculate the distance between two locations from lat and long values
    double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * 6371;

        return distance;
    }


    //Requests permission to use current location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else{
                Toast.makeText(this, "Location permission is denied", Toast.LENGTH_SHORT).show();
            }
            }
        }






    }
