package com.midterm.appchatt.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.content.Intent;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midterm.appchatt.R;
import com.midterm.appchatt.model.ThemeType;
import com.midterm.appchatt.model.User;
import com.midterm.appchatt.databinding.ActivityMainBinding;
import com.midterm.appchatt.ui.adapter.UserAdapter;
import com.midterm.appchatt.ui.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private UserAdapter adapter;
    private TextView txtWelcome;
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        setupAuth();

        setupRecyclerView();
        observeData();

        startActivity(new Intent(this, MainMessage.class));

        finish();
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
        binding.recyclerUsers.setAdapter(adapter);
        binding.recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void observeData() {
        viewModel.getUsers().observe(this, users -> {
            binding.progressBar.setVisibility(View.GONE);
            // Filter out current user from the list
            users.removeIf(user -> user.getUserId().equals(currentUser.getUserId()));
            adapter.submitList(users);
        });

        viewModel.getError().observe(this, error -> {
            // Show error message
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Update user status to offline before logging out
        viewModel.updateUserStatus(currentUser.getUserId(), "offline");
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentUser != null) {
            viewModel.updateUserStatus(currentUser.getUserId(), "offline");
        }
    }
}