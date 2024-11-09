package com.midterm.appchatt.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.midterm.appchatt.data.firebase.FirebaseAuthHelper;
import com.midterm.appchatt.model.User;

public class AuthRepository {
    private final FirebaseAuthHelper authHelper;
    private final MutableLiveData<User> currentUser;
    private final MutableLiveData<String> errorMessage;

    public AuthRepository() {
        authHelper = new FirebaseAuthHelper();
        currentUser = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public void loginUser(String email, String password) {
        authHelper.loginUser(email, password, new FirebaseAuthHelper.OnLoginCompleteListener() {
            @Override
            public void onSuccess(User user) {
                currentUser.setValue(user);
            }

            @Override
            public void onFailure(Exception exception) {
                errorMessage.setValue(exception.getMessage());
            }
        });
    }

    public void registerUser(String email, String password, String displayName) {
        authHelper.registerUser(email, password, displayName,
                new FirebaseAuthHelper.OnRegistrationCompleteListener() {
                    @Override
                    public void onSuccess(User user) {
                        currentUser.setValue(user);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        // Handle error
                    }
                });
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public void loadUserData(String userId) {
        authHelper.loadUserData(userId, new FirebaseAuthHelper.OnUserDataListener() {
            @Override
            public void onSuccess(User user) {
                currentUser.setValue(user);
            }

            @Override
            public void onFailure(Exception exception) {
                errorMessage.setValue(exception.getMessage());
            }
        });
    }

    public void updateUserAvatar(String userId, String avatarUrl) {
        authHelper.updateUserAvatar(userId, avatarUrl);
        // Cập nhật lại currentUser với avatar mới
        User currentUserValue = currentUser.getValue();
        if (currentUserValue != null) {
            currentUserValue.setAvatarUrl(avatarUrl);
            currentUser.setValue(currentUserValue);
        }
    }
}
