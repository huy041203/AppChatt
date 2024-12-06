package com.midterm.appchatt.data.repository;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.midterm.appchatt.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageRepository {
    private final FirebaseDatabase database;
    private final FirebaseStorage storage;
    private final FirebaseFirestore firestore;
    private final DatabaseReference messagesRef;
    private final DatabaseReference usersRef;
    private final List<ValueEventListener> activeListeners;
    private final List<ChildEventListener> activeChildListeners;

    public MessageRepository() {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        messagesRef = database.getReference("messages");
        activeListeners = new ArrayList<>();
        activeChildListeners = new ArrayList<>();
        usersRef = database.getReference("users");
    }

    public void sendMessage(String chatId, Message message) {
        String messageId = UUID.randomUUID().toString();
        message.setMessageId(messageId);
        
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .document(messageId)
            .set(message)
            .addOnSuccessListener(aVoid -> {
                Log.d("MessageRepository", "Message sent successfully with ID: " + messageId);
            })
            .addOnFailureListener(e -> {
                Log.e("MessageRepository", "Error sending message", e);
            });
    }

    public LiveData<List<Message>> getMessages(String chatId) {
        MutableLiveData<List<Message>> messages = new MutableLiveData<>();
        
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener((value, error) -> {
                if (error != null) {
                    Log.e("MessageRepository", "Error getting messages", error);
                    return;
                }

                List<Message> messageList = new ArrayList<>();
                if (value != null) {
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Message message = doc.toObject(Message.class);
                        if (message != null) {
                            messageList.add(message);
                        }
                    }
                }
                messages.setValue(messageList);
            });

        return messages;
    }

    public void deleteMessage(String chatId, String messageId) {
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .document(messageId)
            .delete()
            .addOnSuccessListener(aVoid -> 
                Log.d("MessageRepository", "Message successfully deleted"))
            .addOnFailureListener(e -> 
                Log.e("MessageRepository", "Error deleting message", e));
    }

    public void updateMessageStatus(String chatId, String messageId, boolean isRead) {
        messagesRef.child(chatId).child(messageId).child("read").setValue(isRead);
    }

    public Task<String> uploadImage(Uri imageUri) {
        String imageName = UUID.randomUUID().toString();
        StorageReference imageRef = storage.getReference().child("chat_images").child(imageName);

        return imageRef.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                })
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return task.getResult().toString();
                });
    }

    public void observeUserStatus(String userId, UserStatusCallback callback) {
        ValueEventListener statusListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Boolean isOnline = snapshot.getValue(Boolean.class);
                callback.onStatusChanged(isOnline != null && isOnline);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onStatusChanged(false);
            }
        };

        usersRef.child(userId).child("online").addValueEventListener(statusListener);
        activeListeners.add(statusListener);
    }

    public void removeAllListeners() {
        for (ValueEventListener listener : activeListeners) {
            usersRef.removeEventListener(listener);
        }
        activeListeners.clear();

        for (ChildEventListener listener : activeChildListeners) {
            messagesRef.removeEventListener(listener);
        }
        activeChildListeners.clear();
    }

    private int findMessageIndex(List<Message> messages, String messageId) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMessageId().equals(messageId)) {
                return i;
            }
        }
        return -1;
    }

    public interface UserStatusCallback {
        void onStatusChanged(boolean isOnline);
    }
}