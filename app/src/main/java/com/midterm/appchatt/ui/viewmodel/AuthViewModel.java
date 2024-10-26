package com.midterm.appchatt.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.midterm.appchatt.data.repository.AuthRepository;
import com.midterm.appchatt.model.User;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;

    public AuthViewModel() {
        authRepository = new AuthRepository();
    }

    public void loginUser(String email, String password) {
        authRepository.loginUser(email, password);
    }
    public void registerUser(String email, String password, String displayName) {
        authRepository.registerUser(email, password, displayName);
    }
    public LiveData<String> getErrorMessage() {
        return authRepository.getErrorMessage();
    }

    public LiveData<User> getCurrentUser() {
        return authRepository.getCurrentUser();
    }
}
