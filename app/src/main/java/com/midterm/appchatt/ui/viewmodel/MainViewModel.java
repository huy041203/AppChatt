package com.midterm.appchatt.ui.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.appchatt.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public LiveData<List<User>> getUsers() {
        loadUsers();
        return users;
    }

    public LiveData<String> getError() {
        return error;
    }

    private void loadUsers() {
        Log.d("MainViewModel", "Loading users from database");
        database.getReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("MainViewModel", "User data changed, snapshot size: " + snapshot.getChildrenCount());
                        List<User> userList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                userList.add(user);
                            }
                        }
                        Log.d("MainViewModel", "Loaded users: " + userList.size());
                        users.setValue(userList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("MainViewModel", "Database error: " + error.getMessage());
                        MainViewModel.this.error.setValue(error.getMessage());
                    }
                });
    }

    public void updateUserStatus(String userId, String status) {
        database.getReference("users")
                .child(userId)
                .child("status")
                .setValue(status);
    }
}
