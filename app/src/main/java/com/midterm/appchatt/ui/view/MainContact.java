package com.midterm.appchatt.ui.view;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.midterm.appchatt.databinding.MainContactBinding;
import com.midterm.appchatt.model.Contact;
import com.midterm.appchatt.model.User;
import com.midterm.appchatt.ui.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainContact {
    private MainContactBinding binding;
    private List<Contact> contactList;
    private ContactAdapter adapter;
    private static MainContact _instance;
    private AppCompatActivity activity;

    private MainContact(AppCompatActivity activity) {
        this.activity = activity;
        binding = MainContactBinding.inflate(activity.getLayoutInflater());

        contactList = new ArrayList<>();
        adapter = new ContactAdapter(contactList);

        binding.rvList.setAdapter(adapter);
        binding.rvList.setLayoutManager(new LinearLayoutManager(activity));

        configSearchBar();
    }

    private void configSearchBar() {
        // Search bar configuration.
        binding.searchBarView.searchBar.addTextChangedListener(new TextWatcher() {

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

    public static MainContact get_instance(AppCompatActivity activity) {
        if (_instance == null) {
            _instance = new MainContact(activity);
        }
        return _instance;
    }

    public MainContactBinding getBinding() {
        return binding;
    }
}
