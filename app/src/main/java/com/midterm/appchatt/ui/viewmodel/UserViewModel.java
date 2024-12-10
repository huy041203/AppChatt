package com.midterm.appchatt.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.midterm.appchatt.data.repository.UserRepository;
import com.midterm.appchatt.model.User;

public class UserViewModel extends ViewModel {
    private final UserRepository repository;

    public UserViewModel() {
        repository = new UserRepository();
    }

    public LiveData<User> findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }

    public LiveData<User> getUserById(String userId) {
        return repository.getUserById(userId);
    }
}