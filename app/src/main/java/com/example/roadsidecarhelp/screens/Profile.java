package com.example.roadsidecarhelp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadsidecarhelp.R;
import com.example.roadsidecarhelp.database.DBHelper;
import com.google.android.material.textfield.TextInputEditText;

public class Profile extends AppCompatActivity {

//button to update profile
    Button btnupdate;
    //view user details id, password etc
    TextView jtxtid,sesedtemail,sesedtpass;

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextInputEditText jedtname,jedtemail,jedtpassword,
            jedtaddress,jedtmoblieno,jedtusername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        jedtname= findViewById(R.id.xmluserregnameup);
        jedtemail = findViewById(R.id.xmluserregemailup);
        jedtpassword = findViewById(R.id.xmluserregpasswordup);
        jedtaddress = findViewById(R.id.xmluserregaddressup);
        jedtmoblieno = findViewById(R.id.xmluserregphonenoup);
        jedtusername = findViewById(R.id.xmluserregusernmeup);
        btnupdate = findViewById(R.id.xmluserbtnupdate);
        jtxtid = findViewById(R.id.sessionid);
        sesedtemail= findViewById(R.id.sessionemail);
        sesedtpass = findViewById(R.id.sessionpassword);


        dbHelper = new DBHelper(this);

        sharedPreferences = getSharedPreferences("Data",MODE_PRIVATE);
        editor= sharedPreferences.edit();

        sesedtemail.setText(sharedPreferences.getString("loemail",""));
        sesedtpass.setText(sharedPreferences.getString("lopasss",""));

        String getpemail=sesedtemail.getText().toString();
        String getppass=sesedtpass.getText().toString();

        //create cursor object
        Cursor cur = gettingprofiledata(getpemail,getppass);
        while (cur.moveToNext()){

            jtxtid.setText(cur.getString(0));
            jedtemail.setText(cur.getString(1));
            jedtpassword.setText(cur.getString(2));
            jedtname.setText(cur.getString(3));
            jedtusername.setText(cur.getString(4));
            jedtmoblieno.setText(cur.getString(5));
            jedtaddress.setText(cur.getString(6));


        }

//update button
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            //updates user details
            public void onClick(View view) {
                String fetchid= jtxtid.getText().toString();
//check if data is updated
                boolean res=updateprofiledata(fetchid);
                if(res==true){
                    Toast.makeText(Profile.this,"profile updated",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Profile.this,"profile  not updated,try again",Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    //check data
    private Cursor gettingprofiledata(String email, String pasword) {
        sqLiteDatabase=dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where email=? and password=?",new String[]{email,pasword});
        return cursor;

    }



    //edit user
    private boolean updateprofiledata(String userid) {
        sqLiteDatabase=dbHelper.getWritableDatabase();

        String getemail =jedtemail.getText().toString();
        String getpassword =jedtpassword.getText().toString();
        String getname =jedtname.getText().toString();
        String getusername =jedtusername.getText().toString();
        String getcontact =jedtmoblieno.getText().toString();
        String getaddress =jedtaddress.getText().toString();


        ContentValues cv = new ContentValues();
        cv.put("email", getemail);
        cv.put("password",getpassword);
        cv.put("name",getname);
        cv.put("username",getusername);
        cv.put("contact",getcontact);
        cv.put("address",getaddress);



        long r = sqLiteDatabase.update("users",cv,"id=?",new String[]{userid});
        if(r == -1){
            return false;
        }
        else {
            return true;
        }
    }
}



