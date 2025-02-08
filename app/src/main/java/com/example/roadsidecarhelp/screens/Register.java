package com.example.roadsidecarhelp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadsidecarhelp.MainActivity;
import com.example.roadsidecarhelp.R;
import com.example.roadsidecarhelp.database.DBHelper;
import com.example.roadsidecarhelp.model.Users;
import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {


//fields to enter user details
    TextInputEditText jedtname,jedtemail,jedtpassword,
            jedtaddress,jedtmoblieno,jedtusername;
    //button to register
    Button jbtnregister;
    //link to login
    TextView jtxtlogin;

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

//check if user is already registered
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



//fields to enter user details
        jedtname= findViewById(R.id.xmluserregname);
        jedtemail = findViewById(R.id.xmluserregemail);
        jedtpassword = findViewById(R.id.xmluserregpassword);
        jedtaddress = findViewById(R.id.xmluserregaddress);
        jedtmoblieno = findViewById(R.id.xmluserregphoneno);
        jedtusername = findViewById(R.id.xmluserregusernme);
        jbtnregister = findViewById(R.id.xmluserregbtn);
        jtxtlogin = findViewById(R.id.txtlogin);


        dbHelper = new DBHelper(Register.this);


//link to login
        jtxtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
                finish();
            }
        });

//register button
        jbtnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//check if all fields are filled
                String getemail =jedtemail.getText().toString();
                String getpassword =jedtpassword.getText().toString();
                String getname =jedtname.getText().toString();
                String getusername =jedtusername.getText().toString();
                String getcontact =jedtmoblieno.getText().toString();
                String getaddress =jedtaddress.getText().toString();



//check if all fields are filled
                if (getname.isEmpty()||getemail.isEmpty()||getpassword.isEmpty()||
                        getusername.isEmpty()||getcontact.isEmpty()
                        ||getaddress.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
                    return;
                } else {

//check if user is already registered
                    boolean res= checkalreadyregistered(getemail);


                    if(res == true){

                        Toast.makeText(getApplicationContext(), "Already Registered with this email", Toast.LENGTH_SHORT).show();
                    }else{

//insert data into database
                        Users users = new Users(getemail,getpassword,getname,getusername,getcontact,getaddress);

                        long result = dbHelper.insertusers(users);

                        if(result >0 ){
                            Toast.makeText(Register.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Register.this,Login.class));
                            finish();

                        }
                        else{
                            Toast.makeText(Register.this," Not registered, Try Again",Toast.LENGTH_LONG).show();
                        }
                    }




                }
            }
        });









    }

    private boolean checkalreadyregistered(String getemail) {

        sqLiteDatabase=dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where email=?",
                new String[]{getemail});

        if(cursor.getCount()>0){
            return  true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(Register.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}