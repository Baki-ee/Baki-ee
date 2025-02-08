package com.example.roadsidecarhelp.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roadsidecarhelp.R;
import com.example.roadsidecarhelp.database.ContactsDbHelper;
import com.example.roadsidecarhelp.model.Contact;


public class EditContactActivity extends AppCompatActivity {
//edit text fields to edit name phone no....
    private EditText editTextName, editTextPhone, editTextRelationship;
    private ContactsDbHelper dbHelper;
    private int contactId;
//sets layout for edit text and save button uses save button to update database
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editTextName = findViewById(R.id.edit_text_name);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextRelationship = findViewById(R.id.edit_text_relationship);
        Button saveButton = findViewById(R.id.save_contact);
        dbHelper = new ContactsDbHelper(this);

        contactId = getIntent().getIntExtra("contactId", -1);
        Contact contact;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            contact = dbHelper.getAllContacts().stream().filter(c -> c.getId() == contactId).findFirst().orElse(null);
        } else {
            contact = null;
        }

        if (contact != null) {
            editTextName.setText(contact.getName());
            editTextPhone.setText(contact.getPhoneNumber());
            editTextRelationship.setText(contact.getRelationship());
        }

        saveButton.setOnClickListener(v -> {
            contact.setName(editTextName.getText().toString());
            contact.setPhoneNumber(editTextPhone.getText().toString());
            contact.setRelationship(editTextRelationship.getText().toString());
            dbHelper.updateContact(contact);

            finish(); // Close the activity after updating
        });
    }
}
