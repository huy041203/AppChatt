package com.midterm.appchatt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.midterm.appchatt.R;
import com.midterm.appchatt.ui.viewmodel.AuthViewModel;

public class SignUpActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText usernameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button signUpButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logoutapp);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        initializeViews();
        setupListeners();
        observeViewModel();
    }

    private void initializeViews() {
        usernameInput = findViewById(R.id.edt_username);
        emailInput = findViewById(R.id.edt_email);
        passwordInput = findViewById(R.id.edt_password);
        confirmPasswordInput = findViewById(R.id.edt_confirm_password);
        signUpButton = findViewById(R.id.btn_login);
        backButton = findViewById(R.id.iv_back);
    }

    private void setupListeners() {
        signUpButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            if (validateInput(username, email, password, confirmPassword)) {
                authViewModel.registerUser(email, password, username);
            }
        });
        backButton.setOnClickListener(v -> {
           Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
           startActivity(intent);
        });
    }

    private void observeViewModel() {
        authViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
        authViewModel.getErrorMessage().observe(this, error -> {
            hideLoading();
            if (error != null) {
                showError(error);
            }
        });
    }

    private boolean validateInput(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty()) {
            usernameInput.setError("Username is required");
            return false;
        }
        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            return false;
        }
        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            return false;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("Confirm password is required");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void showLoading() {
        // Hiển thị ProgressBar
        // Disable các button
    }

    private void hideLoading() {
        // Ẩn ProgressBar
        // Enable các button
    }

    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}