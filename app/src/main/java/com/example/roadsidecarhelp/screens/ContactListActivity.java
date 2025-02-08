package com.example.roadsidecarhelp.screens;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.roadsidecarhelp.R;
import com.example.roadsidecarhelp.adapter.ContactsAdapter;
import com.example.roadsidecarhelp.database.ContactsDbHelper;
import com.example.roadsidecarhelp.model.Contact;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private ArrayList<Contact> contactsList;
    private ContactsDbHelper dbHelper;
//resumes work after edit/add updates the contact list
    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
        adapter.notifyDataSetChanged();
    }
    //passes the contact id to editcontactactivity and deletes the contact from database
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        recyclerView = findViewById(R.id.recycler_view_contacts);
        contactsList = new ArrayList<>();
        dbHelper = new ContactsDbHelper(this);

        adapter = new ContactsAdapter(this, contactsList, new ContactsAdapter.OnContactClickListener() {
           //add contact button opens the addcontactactivity
            @Override
            public void onEditContact(Contact contact) {

                Intent intent = new Intent(ContactListActivity.this, EditContactActivity.class);
                intent.putExtra("contactId", contact.getId());
                startActivity(intent);
            }
//Clears the existing contact list and reloads it from the database.
            @Override
            public void onDeleteContact(Contact contact) {
                dbHelper.deleteContact(contact.getId());
                loadContacts();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.add_contact).setOnClickListener(v -> {
            // Open add contact activity or dialog
            Intent intent = new Intent(ContactListActivity.this, AddContactActivity.class);
            startActivity(intent);
        });

        loadContacts();
    }

    private void loadContacts() {
        contactsList.clear();
        contactsList.addAll(dbHelper.getAllContacts());
        adapter.notifyDataSetChanged();
    }

}