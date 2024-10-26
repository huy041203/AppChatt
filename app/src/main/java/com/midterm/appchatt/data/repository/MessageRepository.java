package com.midterm.appchatt.data.repository;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private final DatabaseReference messagesRef;
    private final DatabaseReference usersRef;
    private final List<ValueEventListener> activeListeners;
    private final List<ChildEventListener> activeChildListeners;

    public MessageRepository() {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        messagesRef = database.getReference("messages");
        usersRef = database.getReference("users");
        activeListeners = new ArrayList<>();
        activeChildListeners = new ArrayList<>();
    }

    public LiveData<List<Message>> getMessages(String chatId) {
        MutableLiveData<List<Message>> messages = new MutableLiveData<>(new ArrayList<>());

        ChildEventListener messageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    message.setMessageId(snapshot.getKey());
                    List<Message> currentMessages = messages.getValue();
                    currentMessages.add(message);
                    messages.setValue(currentMessages);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                Message updatedMessage = snapshot.getValue(Message.class);
                if (updatedMessage != null) {
                    updatedMessage.setMessageId(snapshot.getKey());
                    List<Message> currentMessages = messages.getValue();
                    int index = findMessageIndex(currentMessages, snapshot.getKey());
                    if (index != -1) {
                        currentMessages.set(index, updatedMessage);
                        messages.setValue(currentMessages);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                List<Message> currentMessages = messages.getValue();
                int index = findMessageIndex(currentMessages, snapshot.getKey());
                if (index != -1) {
                    currentMessages.remove(index);
                    messages.setValue(currentMessages);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
                // Not needed for basic chat functionality
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        };

        messagesRef.child(chatId).addChildEventListener(messageListener);
        activeChildListeners.add(messageListener);

        return messages;
    }

    public void sendMessage(String chatId, Message message) {
        String messageId = messagesRef.child(chatId).push().getKey();
        if (messageId != null) {
            message.setMessageId(messageId);
            message.setTimestamp(System.currentTimeMillis());
            messagesRef.child(chatId).child(messageId).setValue(message);
        }
    }

    public void deleteMessage(String chatId, String messageId) {
        messagesRef.child(chatId).child(messageId).removeValue();
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