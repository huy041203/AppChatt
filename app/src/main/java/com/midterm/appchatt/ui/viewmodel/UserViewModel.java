package com.midterm.appchatt.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.appchatt.model.User;

public class UserViewModel extends ViewModel {
    private DatabaseReference database;

    public UserViewModel() {
        // Khởi tạo reference đến Realtime Database
        database = FirebaseDatabase.getInstance().getReference();
    }

    public LiveData<User> findUserByEmail(String email) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        // Query trong Realtime Database
        database.child("users")
                .orderByChild("email")
                .equalTo(email)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Lấy user đầu tiên tìm được
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                if (user != null) {
                                    // Đảm bảo set userId từ key của node
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
}