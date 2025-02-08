package com.example.roadsidecarhelp.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roadsidecarhelp.R;
import com.example.roadsidecarhelp.database.ContactsDbHelper;
import com.example.roadsidecarhelp.model.Contact;


public class AddContactActivity extends AppCompatActivity {
//input fields where user will enter data
    private EditText editTextName, editTextPhone, editTextRelationship;
    private ContactsDbHelper dbHelper;
//called when activity is created initializes ui sets activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
//finds UI components
        editTextName = findViewById(R.id.edit_text_name);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextRelationship = findViewById(R.id.edit_text_relationship);
        Button saveButton = findViewById(R.id.save_contact);
        dbHelper = new ContactsDbHelper(this);
// retrievs data from edit text fields saves it creates new contact
        saveButton.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String phone = editTextPhone.getText().toString();
            String relationship = editTextRelationship.getText().toString();

            Contact contact = new Contact(0, name, phone, relationship);
            dbHelper.addContact(contact);
            finish(); // Close the activity after saving
        });
    }
}
