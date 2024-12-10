package com.midterm.appchatt.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.appchatt.model.User;
import com.midterm.appchatt.utils.NoUserFoundException;
import com.midterm.appchatt.utils.RetrivingDataException;
import com.midterm.appchatt.utils.UpdatingEmailException;

public class FirebaseAuthHelper {
    private final FirebaseAuth mAuth;
    private final DatabaseReference userRef;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static String current_password;

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

                        // Gan de phuc vu cho re-auth.
                        current_password = password;


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


                        // Gan de phuc vu cho re-auth.
                        current_password = password;



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

    public void updateUserDisplayName(String displayName) {
        if (mAuth.getCurrentUser() != null) {
            database.getReference("users").child(mAuth.getCurrentUser().getUid())
                    .child("displayName").setValue(displayName);
        }
    }

    public void updateUserPassword(String password) {
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().updatePassword(password);
        }
    }

    public void updateUserEmail(String email, OnUpdateEmailListener listener) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            listener.onFailure(new UpdatingEmailException("User not authenticated!"));
            return;
        }

        reAuthenticate(user, success -> {
            if (success) {
                Log.d("IDEBUG", "Email: " + user.getEmail());
                Log.d("IDEBUG", "Password: " + current_password);
                user.updateEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                listener.onSuccess();
                                database.getReference("users").child(user.getUid())
                                        .child("email").setValue(email);
                            } else {
                                Exception e = task.getException();
                                if (e instanceof FirebaseAuthUserCollisionException) {
                                    listener.onFailure(new UpdatingEmailException("Email already in use!"));
                                    Log.d("IDEBUG", "Email already in use");
                                } else {
                                    listener.onFailure(new UpdatingEmailException("Failed to update email!"));
                                    Log.d("IDEBUG", "Failed to update email!");
                                }
                            }
                        });
            } else {
                listener.onFailure(new UpdatingEmailException("Failed to re-authenticate!"));
                Log.d("IDEBUG", "Failed to re-authentication");
            }
        });
    }

    private void reAuthenticate(FirebaseUser user, OnReAuthListener listener) {
        if (user.getEmail() == null) {
            listener.onReAuthComplete(false);
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), current_password);
        user.reauthenticate(credential)

                .addOnCompleteListener(task -> listener.onReAuthComplete(task.isSuccessful()));
    }

    public void retrieveUserData(final UserDataCallback callback) {
        String uid;
        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid();
        } else {
            callback.onError(new NoUserFoundException("Can't get current user!!"));
            return;
        }
        database.getReference("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String displayName = snapshot.child("displayName").getValue(String.class);
                            String email = snapshot.child("email").getValue(String.class);
                            callback.onUserDataRetrived(displayName, email);
                        } else {
                            callback.onUserDataRetrived(null, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.toException());
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

    public interface UserDataCallback {
        void onUserDataRetrived(
                String displayName,
                String email
                );
        void onError(Exception exception);
    }

    public interface OnReAuthListener {
        void onReAuthComplete(boolean success);
    }

    public interface OnUpdateEmailListener {
        void onSuccess();
        void onFailure(Exception exception);
    }
}
