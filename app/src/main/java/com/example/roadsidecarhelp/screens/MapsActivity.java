package com.example.roadsidecarhelp.screens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.roadsidecarhelp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //provides access to devices current location
    private FusedLocationProviderClient fusedLocationClient;
    private Button refreshButton;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get map fragment and load map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        refreshButton = findViewById(R.id.btn_refresh_location);
        refreshButton.setOnClickListener(v -> refreshLocation());
    }
//used when map is ready. checks neccesary permissions and gets location
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Request location access
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Get initial location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                updateMapLocation(location);
            }
        });
    }
//fetches current location when refresh button is pressed
    private void refreshLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                //updates map location with new location
                public void onSuccess(Location location) {
                    if (location != null) {
                        updateMapLocation(location);
                    } else {
                        Toast.makeText(MapsActivity.this, "Location not found!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
//updates map location with users new location
    private void updateMapLocation(Location location) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }
//checks if permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            refreshLocation();
        }
    }
}
