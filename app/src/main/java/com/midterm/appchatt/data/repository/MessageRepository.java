package com.midterm.appchatt.data.firebase;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class MessageRepository {
    private final FirebaseDatabase database;
    private final FirebaseStorage storage;
    private final MessageDao messageDao; // For local caching

    public MessageRepository(FirebaseDatabase database, FirebaseStorage storage, MessageDao messageDao) {
        this.database = database;
        this.storage = storage;
        this.messageDao = messageDao;
    }

    public ChildEventListener loadMessages(String chatId, int pageSize,
                                           OnMessagesLoadedCallback callback,
                                           OnErrorCallback errorCallback) {
        Query query = database.getReference("messages/" + chatId)
                .limitToLast(pageSize);

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    // Cache message locally
                    messageDao.insertMessage(message);
                    callback.onMessagesLoaded(Collections.singletonList(message));
                }
            }
            // ... other callback implementations
        };

        query.addChildEventListener(listener);
        return listener;
    }

    public void sendMessage(String chatId, Message message, OnCompleteListener<Void> listener) {
        // First save locally
        messageDao.insertMessage(message);

        // Then sync with server
        database.getReference("messages/" + chatId)
                .push()
                .setValue(message)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // Mark message as failed in local DB
                        message.setStatus(Message.STATUS_FAILED);
                        messageDao.updateMessage(message);
                    }
                    listener.onComplete(task);
                });
    }
}
