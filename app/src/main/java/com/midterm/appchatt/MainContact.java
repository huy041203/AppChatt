package com.midterm.appchatt;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

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


        //Search bar configuration.
        binding.searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    adapter.finalizeFilteringAction();
                }

                List<Contact> filteredList = new ArrayList<>();
                adapter.notifyThereIsAFilteringAction(filteredList);
                for (Contact contact : contactList) {
                    if (contact.getDisplayName().toLowerCase()
                            .contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(contact);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}