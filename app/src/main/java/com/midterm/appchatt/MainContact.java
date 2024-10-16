package com.midterm.appchatt;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.midterm.appchatt.adapters.ContactAdapter;
import com.midterm.appchatt.databinding.MainContactBinding;
import com.midterm.appchatt.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainContact extends AppCompatActivity {

    private MainContactBinding binding;
    private List<Contact> contactList;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = MainContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navbarView.contactIcon.setImageResource(R.drawable.contact_list_highlight);

        contactList = new ArrayList<>();
        adapter = new ContactAdapter(contactList);
        binding.rvList.setAdapter(adapter);
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));

        // Add some templates.
        contactList.add(new Contact("", "Ai Nhien"));
        contactList.add(new Contact("", "Duy An"));
        contactList.add(new Contact("", "Gia Bao"));
        contactList.add(new Contact("", "Hoang Quyen"));
        contactList.add(new Contact("", "Nhat Nguyen"));
        contactList.add(new Contact("", "Ngoc Huy"));
        contactList.add(new Contact("", "Mommy"));

    }
}