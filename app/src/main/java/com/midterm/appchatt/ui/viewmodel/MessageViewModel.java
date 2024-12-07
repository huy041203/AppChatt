package com.midterm.appchatt.ui.viewmodel;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.midterm.appchatt.data.repository.MessageRepository;
import com.midterm.appchatt.model.Message;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import java.util.UUID;

public class MessageViewModel extends ViewModel {
    private final MessageRepository repository;
    private MutableLiveData<List<Message>> messages;
    private MutableLiveData<Message> lastMessage;
    private MutableLiveData<Boolean> userStatus;
    private MutableLiveData<Long> lastActive;

    public MessageViewModel() {
        repository = new MessageRepository();
        messages = new MutableLiveData<>();
        lastMessage = new MutableLiveData<>();
        userStatus = new MutableLiveData<>();
        lastActive = new MutableLiveData<>();
    }

    public LiveData<List<Message>> getMessages() {
        if (messages == null) {
            messages = new MutableLiveData<>();
        }
        return messages;
    }

    public LiveData<Message> getLastMessage(String chatId) {
        if (lastMessage == null) {
            lastMessage = new MutableLiveData<>();
        }
        return repository.getLastMessage(chatId);
    }

    public void loadMessages(String chatId) {
        repository.getMessages(chatId).observeForever(messageList -> {
            messages.setValue(messageList);
            if (messageList != null && !messageList.isEmpty()) {
                lastMessage.setValue(messageList.get(messageList.size() - 1));
            }
        });
    }

    public void sendMessage(String chatId, Message message) {
        repository.sendMessage(chatId, message);
        lastMessage.setValue(message);
    }

    public void deleteMessage(String chatId, String messageId) {
        repository.deleteMessage(chatId, messageId);
    }

    private boolean isMessageValid(Message message) {
        return message.getSenderId() != null && 
               message.getContent() != null && 
               !message.getContent().isEmpty();
    }

    public LiveData<Boolean> getUserStatus() {
        return userStatus;
    }

    public LiveData<Long> getLastActive() {
        return lastActive;
    }

    public void observeUserStatus(String userId) {
        repository.observeUserStatus(userId, (status, lastActiveTime) -> {
            userStatus.setValue(status.equals("online"));
            lastActive.setValue(lastActiveTime);
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
        })
                .addOnFailureListener(e -> {
                    Log.d("SendingImage", "Image upload failed " + e.getMessage(), e);
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