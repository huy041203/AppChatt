package com.midterm.appchatt.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.appchatt.model.User;

public class UserRepository {
    private final DatabaseReference database;

    public UserRepository() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public LiveData<User> findUserByEmail(String email) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        database.child("users")
                .orderByChild("email")
                .equalTo(email)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                if (user != null) {
                                    user.setUserId(userSnapshot.getKey());
                                    userLiveData.setValue(user);
                                    break;
                                }
                            }
                        } else {
                            userLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        userLiveData.setValue(null);
                    }
                });

        return userLiveData;
    }

    public LiveData<User> getUserById(String userId) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        
        database.child("users").child(userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        user.setUserId(snapshot.getKey());
                    }
                    userLiveData.setValue(user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    userLiveData.setValue(null);
                }
            });
        
        return userLiveData;
    }
} 