package com.example.roadsidecarhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.roadsidecarhelp.screens.Login;
import com.example.roadsidecarhelp.screens.Register;

public class MainActivity extends AppCompatActivity {

    Button jbtnreg,jbtnlog;
//store session data  login details contains UI for login and reg
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    //initializes buttons
        jbtnreg = findViewById(R.id.mbtnregister);
        jbtnlog = findViewById(R.id.mbtnlogin);
// opens register activity
        jbtnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,
                        Register.class);
                startActivity(i);
                finish();
            }
        });

//redirects to login page
        jbtnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,
                        Login.class);
                startActivity(i);
                finish();
            }
        });

    }
}