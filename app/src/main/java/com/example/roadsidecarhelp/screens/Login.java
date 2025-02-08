package com.example.roadsidecarhelp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadsidecarhelp.Dashboard;
import com.example.roadsidecarhelp.MainActivity;
import com.example.roadsidecarhelp.R;
import com.example.roadsidecarhelp.database.DBHelper;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {



//acts as a link to registration screen
    TextView jtxtregister;
    //fields to enter email and password
    TextInputEditText jedtloginemail,jedtloginpassword;
    //button to submit login credentials
    Button jbtnlogin;
//to store and retrive email/password from local storage
    //sharedpreference and editor
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
// check login credentials
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



//starts registration activity
        jtxtregister = findViewById(R.id.txtregtxt);
        jedtloginemail = findViewById(R.id.loginedtemail);
        jedtloginpassword = findViewById(R.id.loginedtpass);
        jbtnlogin = findViewById(R.id.btnloginx);

        dbHelper = new DBHelper(this);

        jtxtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,
                        Register.class);
                startActivity(i);
                finish();
            }
        });





        sharedPreferences =getSharedPreferences("Data",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        jbtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            //checks if email and password are correct
            public void onClick(View view) {

                String getemail = jedtloginemail.getText().toString();
                String getpassword = jedtloginpassword.getText().toString();



                if (getemail.isEmpty()||getpassword.isEmpty()){
                    Toast.makeText(Login.this,
                            "Please fill all the fields", Toast.LENGTH_LONG).show();
                }

                else {

                    boolean res= checkemailandpassword(getemail,getpassword);
                    if(res == true){
                        editor.putString("loemail",getemail);
                        editor.putString("lopasss",getpassword);
                        editor.apply();
                        Intent i = new Intent(Login.this,
                                Dashboard.class);
                        startActivity(i);
                        finish();

                    }
                    else {
                        Toast.makeText(Login.this,"invalid  login details",Toast.LENGTH_LONG).show();

                    }




                }
            }
        });



    }


    //check email
    private boolean checkemailandpassword(String email,String pasword) {
        sqLiteDatabase=dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where email=? and password=?",new String[]{email,pasword});
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

        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}