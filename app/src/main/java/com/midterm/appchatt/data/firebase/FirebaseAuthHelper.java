package com.midterm.appchatt.data.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.appchatt.model.User;

public class FirebaseAuthHelper {
    private final FirebaseAuth mAuth;
    private final DatabaseReference userRef;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public FirebaseAuthHelper() {
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");
    }
    public interface OnUserDataListener {
        void onSuccess(User user);
        void onFailure(Exception exception);
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
    public void loadUserData(String userId, OnUserDataListener listener) {
        database.getReference("users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null && (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty())) {
                            user.setAvatarUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fm.facebook.com%2FHKSRVN%2Fposts%2Ffirefly-%25E1%25BA%25A5m-%25C3%25A1p-x-%2F407204798482159%2F&psig=AOvVaw0crZLDWp_dU5suC4B832XX&ust=1731246894472000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCPDVorOzz4kDFQAAAAAdAAAAABAE");

                            updateUserAvatar(userId, user.getAvatarUrl());
                        }
                        listener.onSuccess(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        listener.onFailure(error.toException());
                    }
                });
    }

    public void updateUserAvatar(String userId, String avatarUrl) {
        database.getReference("users").child(userId)
                .child("avatarUrl").setValue(avatarUrl);
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
