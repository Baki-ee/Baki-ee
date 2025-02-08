package com.example.roadsidecarhelp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.roadsidecarhelp.database.ContactsDbHelper;
import com.example.roadsidecarhelp.database.ServiceHelper;
import com.example.roadsidecarhelp.model.Contact;
import com.example.roadsidecarhelp.screens.MapsActivity;
import com.example.roadsidecarhelp.screens.Profile;
import com.example.roadsidecarhelp.screens.ServiceListActivity;
import com.example.roadsidecarhelp.screens.ContactListActivity;
import com.example.roadsidecarhelp.database.DBHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {
    private static final int SMS_PERMISSION_REQUEST_CODE = 101;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    //store session data  login details
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //interacts with database
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Button jbtnlogout, jbtnprofile, jbtnservice, jbtncontact, location;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
// interacting with database
        dbHelper = new DBHelper(this);

        jbtnlogout = findViewById(R.id.btnlogout);
        jbtnprofile = findViewById(R.id.btnprofile);
        jbtnservice = findViewById(R.id.btnservice);
        jbtncontact = findViewById(R.id.btncontact);
        location = findViewById(R.id.location);
// manages user sessions
        sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
//logout clears preferences redirects to login page
        jbtnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.apply();
                startActivity(new Intent(Dashboard.this, MainActivity.class));
                finish();
            }
        });

//location button navigates to maps activity
        location
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Dashboard.this, MapsActivity.class));
                    }
                });
        //profile button navigates to profile activity
        jbtnprofile
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Dashboard.this, Profile.class));
                    }
                });
//redirects to contacts where contacts can be managed and viewed
        jbtncontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ContactListActivity.class));
            }
        });
//redirects to services where services can be managed and viewed
        jbtnservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ServiceListActivity.class));
            }
        });
//sos button
        Button sosButton = findViewById(R.id.sos_button);
        sosButton.setOnClickListener(v -> {

            if (checkLocationPermission()) {
                initiateSOS();
            } else {
                requestLocationPermission();
            }
        });
    }
    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void initiateSOS() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }


        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            // Convert coordinates to a human-readable address
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            fetchNearbyPlaces(latitude,longitude);
            String currentLocation = latitude + "," + longitude;
            String address = getAddressFromLocation(latitude, longitude);
            if (address != null) {
                Toast.makeText(this, "Your location: " + address, Toast.LENGTH_SHORT).show();
                sendEmergencyNotification(address); // Send the address instead of coordinates
            } else {
                Toast.makeText(this, "Unable to get address from location. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
// fetch nearby places from google api
    public void fetchNearbyPlaces(double latitude, double longitude) {
        String apiKey = getString(R.string.map_api_key);
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + latitude + "," + longitude + "&radius=1500&type=gas_station|lodging|restaurant&key=" + apiKey;

        // Use Volley for network operations
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Parse the JSON response
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray results = jsonObject.getJSONArray("results");
                        ServiceHelper databaseHelper = new ServiceHelper(this);

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject place = results.getJSONObject(i);
                            String name = place.getString("name");
                            String type = place.getJSONObject("types").getJSONArray(String.valueOf(0)).getString(0); // Simplification; you might want to handle multiple types
                            double lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            double lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            String contact = "Contact info not provided"; // Placeholder

                            // Insert into SQLite
                            databaseHelper.insertService(name, type, lat, lng, contact);
                            Log.e("Place", name);
                            Log.e("Type", type);
                            Log.e("Lat", String.valueOf(lat));
                            Log.e("Lng", String.valueOf(lng));
                            Log.e("Contact", contact);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Log.e("API Error", error.toString());
                });

        requestQueue.add(stringRequest);
    }

//convert coordinates to address using geocoderclass
    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            // Get the address from the geocoder
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Get the first result
            // Check if any addresses were returned
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                return sb.toString().trim(); // Return the full address
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if unable to get address
    }

    private void sendEmergencyNotification(String location) {
        // Fetch emergency contacts from your database or user preferences
        ArrayList<String> emergencyContacts = getEmergencyContacts();
// Send notifications to emergency contacts
        for (String contact : emergencyContacts) {
            // Send notification logic (e.g., using Firebase Cloud Messaging or SMS)
            String message = "Emergency! I am in a car breakdown situation. My location: " + location;
            Log.e("Contact", contact);
            Log.e("Message", message);
            sendNotificationToContact(contact, message);
        }

        Toast.makeText(this, "Emergency notifications sent!", Toast.LENGTH_SHORT).show();
    }
//get emergency contacts from database
    private ArrayList<String> getEmergencyContacts() {
        // TODO: Fetch emergency contacts from your database or user preferences
        ArrayList<Contact> contacts = new ArrayList<>();
        ContactsDbHelper databaseHelper = new ContactsDbHelper(this);
        contacts = databaseHelper.getAllContacts();
        ArrayList<String> emergencyContacts = new ArrayList<>();
        for (Contact contact : contacts) {
            emergencyContacts.add(contact.getPhoneNumber());
        }
        return emergencyContacts;
    }
//send notification to contact
    private void sendNotificationToContact(String contact, String message) {
        // TODO: Implement the notification sending logic, e.g., using Firebase Cloud Messaging or SMS

        if (checkSMSPermission()) {
            sendSMS(contact, message);
        } else {
            requestSMSPermission(); // Request the permission if not granted
        }
    }
//send sms
    private void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private boolean checkSMSPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSMSPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
    }


    @Override

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    // Handle permission request results
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
        // Handle location permission result
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Permission granted, proceed with location-related operations
            } else {
                Toast.makeText(this, "Location permission required!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with sending SMS
                initiateSOS();
            } else {
                Toast.makeText(this, "SMS permission is required to send messages.", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}