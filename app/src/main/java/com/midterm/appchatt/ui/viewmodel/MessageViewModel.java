package com.midterm.appchatt.ui.viewmodel;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.midterm.appchatt.data.repository.MessageRepository;
import com.midterm.appchatt.model.Message;
import java.util.List;
import android.util.Log;
import java.util.UUID;
import java.util.ArrayList;
import com.google.firebase.firestore.DocumentSnapshot;

public class MessageViewModel extends ViewModel {
    private final MessageRepository repository;
    private MutableLiveData<List<Message>> messages;
    private MutableLiveData<Boolean> userStatus;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MessageViewModel() {
        repository = new MessageRepository();
        messages = new MutableLiveData<>();
        userStatus = new MutableLiveData<>();
    }

    public LiveData<List<Message>> getMessages() {
        if (messages == null) {
            messages = new MutableLiveData<>();
        }
        return messages;
    }

    public LiveData<Boolean> getUserStatus() {
        if (userStatus == null) {
            userStatus = new MutableLiveData<>();
        }
        return userStatus;
    }

    public void loadMessages(String chatId) {
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener((value, error) -> {
                if (error != null) {
                    Log.e("MessageViewModel", "Error loading messages", error);
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
                    messages.setValue(messageList);
                    Log.d("MessageViewModel", "Loaded " + messageList.size() + " messages");
                }
            });
    }

    public void sendMessage(String chatId, Message message) {
        if (message.getSenderId() == null) {
            Log.e("MessageViewModel", "SenderId is null when trying to send message");
            return;
        }

        Log.d("MessageViewModel", "Attempting to send message: " + message.toString());
        Log.d("MessageViewModel", "ChatId: " + chatId);

        String messageId = UUID.randomUUID().toString();
        message.setMessageId(messageId);
        
        if (chatId == null || chatId.isEmpty()) {
            Log.e("MessageViewModel", "ChatId is null or empty");
            return;
        }
        
        if (message.getContent() == null) {
            Log.e("MessageViewModel", "Message content is null");
            return;
        }

        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .document(messageId)
            .set(message)
            .addOnSuccessListener(aVoid -> {
                Log.d("MessageViewModel", "Message sent successfully with ID: " + messageId);
            })
            .addOnFailureListener(e -> {
                Log.e("MessageViewModel", "Error sending message: " + e.getMessage(), e);
            });
    }

    public void sendImage(String chatId, Uri imageUri, String senderId) {
        repository.uploadImage(imageUri).addOnSuccessListener(url -> {
            Message message = new Message(
                    senderId,
                    url,
                    "image"
            );
            sendMessage(chatId, message);
        });
    }

    public void observeUserStatus(String userId) {
        repository.observeUserStatus(userId, isOnline -> {
            userStatus.setValue(isOnline);
        });
    }

    public void deleteMessage(String chatId, String messageId) {
        Log.d("MessageViewModel", "Deleting message - chatId: " + chatId + ", messageId: " + messageId);
        
        if (chatId == null || messageId == null) {
            Log.e("MessageViewModel", "ChatId or MessageId is null");
            return;
        }

        db.collection("chats")
          .document(chatId)
          .collection("messages")
          .document(messageId)
          .delete()
          .addOnSuccessListener(aVoid -> {
              Log.d("MessageViewModel", "Message successfully deleted");
          })
          .addOnFailureListener(e -> {
              Log.e("MessageViewModel", "Error deleting message", e);
          });
    }

    public void markMessageAsRead(String chatId, String messageId) {
        repository.updateMessageStatus(chatId, messageId, true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Clean up any observers or listeners here
        repository.removeAllListeners();
    }
}