package com.midterm.appchatt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.midterm.appchatt.R;
import com.midterm.appchatt.ui.adapter.ContactAdapter;
import com.midterm.appchatt.databinding.MainContactBinding;
import com.midterm.appchatt.model.Contact;
import com.midterm.appchatt.ui.viewmodel.UserViewModel;
import com.midterm.appchatt.utils.NavbarSupport;
import com.midterm.appchatt.ui.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainContact extends AppliedThemeActivity implements
        ContactAdapter.OnUserClickListener {

    private MainContactBinding binding;
    private List<Contact> contactList;
    private ContactAdapter adapter;
    private ContactViewModel contactViewModel;
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = MainContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        setupRecyclerView();
        setupAddContactButton();
        fetchContacts();
        configSearchBar();
        NavbarSupport.setup(this, binding.navbarView);
    }

    private void setupViewModel() {
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }
    private void setupAddContactButton() {
        binding.fabAddContact.setOnClickListener(v -> showAddContactDialog());
    }
    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);

        EditText emailInput = dialogView.findViewById(R.id.editTextEmail);
        EditText nameInput = dialogView.findViewById(R.id.editTextName);

        builder.setView(dialogView)
                .setTitle("Add New Contact")
                .setPositiveButton("Add", (dialog, id) -> {
                    String email = emailInput.getText().toString().trim();
                    String name = nameInput.getText().toString().trim();

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(name)) {
                        findAndAddContact(email, name);
                    } else {
                        Toast.makeText(MainContact.this,
                                "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void findAndAddContact(String email, String name) {

        userViewModel.findUserByEmail(email).observe(this, user -> {
            if (user != null) {
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Tạo contact mới với avatar URL từ user tìm được
                Contact newContact = new Contact(
                        currentUserId,
                        user.getUserId(),
                        name,
                        user.getAvatarUrl() // Sử dụng avatar của user tìm được
                );

                // Thêm contact mới
                contactViewModel.addContact(newContact);

                Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show();

                // Refresh danh sách contact
                fetchContacts();
            } else {

                Toast.makeText(this, "User not found with this email",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupRecyclerView() {
        contactList = new ArrayList<>();
        adapter = new ContactAdapter(contactList, this);
        binding.rvList.setAdapter(adapter);
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchContacts() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        contactViewModel.getContactsForUser(currentUserId).observe(this, contacts -> {


            if (contacts != null && !contacts.isEmpty()) {
                contactList.clear();
                contactList.addAll(contacts);
                adapter.notifyDataSetChanged();


                binding.rvList.setVisibility(View.VISIBLE);
            } else {

                binding.rvList.setVisibility(View.GONE);
            }
        });

    }

    private void configSearchBar() {
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase().trim();

                if (searchText.isEmpty()) {
                    adapter.updateList(contactList);
                    return;
                }

                List<Contact> filteredList = new ArrayList<>();
                for (Contact contact : contactList) {
                    if (contact.getDisplayName().toLowerCase().contains(searchText)) {
                        filteredList.add(contact);
                    }
                }
                adapter.updateList(filteredList);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onUserClick(Contact contact) {
        Intent intent = new Intent(this, UserDetailActivity.class);

        intent.putExtra("name", contact.getDisplayName());
        startActivity(intent);
    }
}