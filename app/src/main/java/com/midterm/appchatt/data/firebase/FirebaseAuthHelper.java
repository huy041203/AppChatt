package com.midterm.appchatt.data.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midterm.appchatt.model.User;

public class FirebaseAuthHelper {
    private final FirebaseAuth mAuth;
    private final DatabaseReference userRef;

    public FirebaseAuthHelper() {
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public void registerUser(String email, String password, String displayName,
                             OnRegistrationCompleteListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().getUser() != null) {
                        String userId = task.getResult().getUser().getUid();
                        User newUser = new User(userId, email, displayName);
                        userRef.child(userId).setValue(newUser)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        listener.onSuccess(newUser);
                                    } else {
                                        listener.onFailure(dbTask.getException());
                                    }
                                });
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }
    public void loginUser(String email, String password, OnLoginCompleteListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().getUser() != null) {
                        String userId = task.getResult().getUser().getUid();
                        // Lấy thông tin user từ database
                        userRef.child(userId).get().addOnCompleteListener(userTask -> {
                            if (userTask.isSuccessful()) {
                                User user = userTask.getResult().getValue(User.class);
                                if (user != null) {
                                    // Cập nhật trạng thái online
                                    user.setStatus("online");
                                    user.setLastActive(System.currentTimeMillis());
                                    userRef.child(userId).setValue(user)
                                            .addOnCompleteListener(updateTask -> {
                                                if (updateTask.isSuccessful()) {
                                                    listener.onSuccess(user);
                                                } else {
                                                    listener.onFailure(updateTask.getException());
                                                }
                                            });
                                } else {
                                    listener.onFailure(new Exception("User data not found"));
                                }
                            } else {
                                listener.onFailure(userTask.getException());
                            }
                        });
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public interface OnLoginCompleteListener {
        void onSuccess(User user);
        void onFailure(Exception exception);
    }

    public interface OnRegistrationCompleteListener {
        void onSuccess(User user);
        void onFailure(Exception exception);
    }
}