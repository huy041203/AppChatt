package com.midterm.appchatt.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.midterm.appchatt.data.firebase.FirebaseAuthHelper;
import com.midterm.appchatt.data.repository.AuthRepository;
import com.midterm.appchatt.model.User;
import com.midterm.appchatt.utils.NoUserFoundException;
import com.midterm.appchatt.utils.UpdatingEmailException;

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


    public void modifyUserEmail(String email, FirebaseAuthHelper.OnUpdateEmailListener listener) {
        authRepository.updateUserEmail(email, listener);
    }

    public void modifyUserDisplayName(String displayName) {
        authRepository.updateUserDisplayName(displayName);
    }

    public void modifyUserPassword(String password) {
        authRepository.updateUserPassword(
                password
        );
    }

    public void getUserData(FirebaseAuthHelper.UserDataCallback callback) {
        authRepository.retriveUserData(callback);

    }

    public LiveData<String> getErrorMessage() {
        return authRepository.getErrorMessage();
    }

    public LiveData<User> getCurrentUser() {
        return authRepository.getCurrentUser();
    }
}
