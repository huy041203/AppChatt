package com.midterm.appchatt.ui.activity;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midterm.appchatt.R;
import com.midterm.appchatt.data.repository.AuthRepository;
import com.midterm.appchatt.databinding.MainMessageBinding;
import com.midterm.appchatt.model.Contact;
import com.midterm.appchatt.model.User;
import com.midterm.appchatt.databinding.ActivityMainBinding;
import com.midterm.appchatt.ui.adapter.UserAdapter;
import com.midterm.appchatt.ui.viewmodel.ContactViewModel;
import com.midterm.appchatt.ui.viewmodel.MainViewModel;
import com.midterm.appchatt.utils.NavbarSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppliedThemeActivity implements UserAdapter.OnUserClickListener {
    private MainMessageBinding binding;
    private MainViewModel viewModel;
    public UserAdapter adapter;
    private FirebaseAuth mAuth;
    private User currentUser;
    private List<User> userList = new ArrayList<>();
    private ContactViewModel contactViewModel;
    private List<Contact> contacts;
    private List<User> filteredList = new ArrayList<>();
    private AuthRepository authRepository = new AuthRepository();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        setupAuth();

        setupRecyclerView();
        observeData();

        configSearchBar();

        NavbarSupport.setup(this, binding.navbarView);
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        fetchContacts();
    }
    private void fetchContacts() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        authRepository.loadUserData(currentUserId);
        contactViewModel.getContactsForUser(currentUserId).observe(this, contacts -> {
            Log.d("MainActivity", "Contacts received: " + (contacts != null ? contacts.size() : "null"));
            if (contacts == null || contacts.isEmpty()) {
                Log.d("MainActivity", "No contacts found for user: " + currentUser.getUserId());
                adapter.submitList(userList);
                return;
            }
            this.contacts = contacts;
        });
    }

    private void setupAuth() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Create current user object
        currentUser = new User(
                firebaseUser.getUid(),
                firebaseUser.getEmail(),
                firebaseUser.getDisplayName()
        );

        // Update user status to online
        viewModel.updateUserStatus(currentUser.getUserId(), "online");
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new UserAdapter(this);
        binding.rvList.setAdapter(adapter);
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void observeData() {
        Log.d("MainActivity", "Start observing data");
        viewModel.getUsers().observe(this, users -> {
            Log.d("MainActivity", "Users updated: " + (users != null ? users.size() : 0));
            binding.progressBar.setVisibility(View.GONE);

            contactViewModel.getContactsForUser(currentUser.getUserId()).observe(this, contacts -> {
                Log.d("MainActivity", "Contacts updated: " + (contacts != null ? contacts.size() : 0));
                List<User> filteredUsers = new ArrayList<>();
                for (User user : users) {
                    for (Contact contact : contacts) {
                        if (user.getUserId().equals(contact.getContactId())) {
                            filteredUsers.add(user);
                            break;
                        }
                    }
                }
                Log.d("MainActivity", "Filtered users: " + filteredUsers.size());
                adapter.submitList(filteredUsers);
            });
        });

        viewModel.getError().observe(this, error -> {
            Log.e("MainActivity", "Error: " + error);
            binding.textError.setVisibility(View.VISIBLE);
            binding.textError.setText(error);
        });
    }

    @Override
    public void onUserClick(User otherUser) {
        // Generate unique chat ID (smaller ID first to ensure consistency)
        String chatId = generateChatId(currentUser.getUserId(), otherUser.getUserId());

        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("otherUser", otherUser);
        intent.putExtra("chatId", chatId);
        startActivity(intent);
    }

    private String generateChatId(String userId1, String userId2) {
        // Always use the same order to generate chat ID
        return userId1.compareTo(userId2) < 0
                ? userId1 + "_" + userId2
                : userId2 + "_" + userId1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentUser != null) {
            viewModel.updateUserStatus(currentUser.getUserId(), "offline");
        }
    }

    private void configSearchBar() {
        // Search bar configuration.
        binding.searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterUsers(charSequence.toString());
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void filterUsers(String query) {
        if (userList == null) {
            return; // Guard against null list
        }

        if (query.isEmpty()) {
            adapter.submitList(new ArrayList<>(userList));
            return;
        }

        filteredList.clear();
        String lowercaseQuery = query.toLowerCase().trim();

        for (User user : userList) {
            if (user.getDisplayName() != null &&
                    user.getDisplayName().toLowerCase().contains(lowercaseQuery)) {
                filteredList.add(user);
            }
        }

        adapter.submitList(new ArrayList<>(filteredList));
    }
}
