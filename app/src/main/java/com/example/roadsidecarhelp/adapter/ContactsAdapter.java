package com.example.roadsidecarhelp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.roadsidecarhelp.R;
import com.example.roadsidecarhelp.model.Contact;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contact> contactsList;
    private OnContactClickListener listener;
//when user interacts with del/edit button
    public interface OnContactClickListener {
        void onEditContact(Contact contact);
        void onDeleteContact(Contact contact);
    }

    @SuppressLint("NotifyDataSetChanged")
    public ContactsAdapter(Context context, ArrayList<Contact> contactsList, OnContactClickListener listener) {
        this.context = context;
        this.contactsList = contactsList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the contact data to the views
        Contact contact = contactsList.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactPhone.setText(contact.getPhoneNumber());
        holder.contactRelationship.setText(contact.getRelationship());
        // Set click listeners for edit and delete buttons
        holder.editButton.setOnClickListener(v -> listener.onEditContact(contact));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteContact(contact));
    }
//get the size of the list
    @Override
    public int getItemCount() {
        return contactsList.size();
    }
//view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactPhone, contactRelationship;
        Button editButton, deleteButton;
//constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactPhone = itemView.findViewById(R.id.contact_phone);
            contactRelationship = itemView.findViewById(R.id.contact_relationship);
            editButton = itemView.findViewById(R.id.edit_contact);
            deleteButton = itemView.findViewById(R.id.delete_contact);
        }
    }
}

