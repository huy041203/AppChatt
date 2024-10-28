package com.midterm.appchatt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.midterm.appchatt.R;
import com.midterm.appchatt.ui.adapter.ContactAdapter;
import com.midterm.appchatt.databinding.MainContactBinding;
import com.midterm.appchatt.model.Contact;
import com.midterm.appchatt.utils.NavbarSupport;

import java.util.ArrayList;
import java.util.List;

public class MainContact extends AppliedThemeActivity {

    private MainContactBinding binding;
    private List<Contact> contactList;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = MainContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contactList = new ArrayList<>();
        adapter = new ContactAdapter(contactList);

        binding.rvList.setAdapter(adapter);
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));

//        // Add some templates.
//        chatList.add(new Chat("", "Nhat Nguyen",
//                "Ủa em?", "14:22"));
//        chatList.add(new Chat("", "Duy An",
//                "OK", "09:34"));
//        chatList.add(new Chat("", "Ngoc Huy",
//                "Đã xong deadline chưa em?", "Yesterday"));
//        chatList.add(new Chat("", "Mommy",
//                "Ngủ đi", "24/09/2023"));
//

        configSearchBar();
        NavbarSupport.setup(this, binding.navbarView);

    }

    private void configSearchBar() {
        // Search bar configuration.
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