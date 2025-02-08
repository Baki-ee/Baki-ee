package com.example.roadsidecarhelp.screens;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roadsidecarhelp.R;
import com.example.roadsidecarhelp.adapter.ServiceAdapter;
import com.example.roadsidecarhelp.database.ServiceHelper;
import com.example.roadsidecarhelp.model.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceListActivity extends AppCompatActivity {
//views service list
    private RecyclerView recyclerView;
    //binds list of services to UI
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;
    Button newService;

    @SuppressLint("MissingInflatedId")
    @Override
    //loads services from database
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        newService=findViewById(R.id.newService);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadServices();
    }

    private void loadServices() {
        // Fetch services from the database
        ServiceHelper databaseHelper = new ServiceHelper(this);
        Cursor cursor = databaseHelper.getAllServices();
        serviceList = new ArrayList<>();
//gets data from data base
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            String type = cursor.getString(2);
            double latitude = cursor.getDouble(3);
            double longitude = cursor.getDouble(4);
            String contact = cursor.getString(5);
            serviceList.add(new Service(name, type, latitude, longitude, contact));
        }
        cursor.close();

        serviceAdapter = new ServiceAdapter(serviceList);
        recyclerView.setAdapter(serviceAdapter);
    }
}
