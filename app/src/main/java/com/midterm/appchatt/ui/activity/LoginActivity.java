package com.midterm.appchatt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.midterm.appchatt.R;
import com.midterm.appchatt.ui.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText emailInput, passwordInput;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginapp);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.registerUser("huy2003@gmail.com", "huy04122003", "Ngoc Huy");
        initializeViews();
        setupListeners();
        observeViewModel();
    }


    private void initializeViews() {
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
//        registerButton = findViewById(R.id.register_button);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            authViewModel.loginUser(email, password);
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
    private boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            return false;
        }
        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
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
