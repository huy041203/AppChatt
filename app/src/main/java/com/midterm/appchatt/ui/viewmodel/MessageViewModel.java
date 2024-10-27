package com.midterm.appchatt.ui.viewmodel;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.midterm.appchatt.data.repository.MessageRepository;
import com.midterm.appchatt.model.Message;
import java.util.List;

public class MessageViewModel extends ViewModel {
    private final MessageRepository repository;
    private MutableLiveData<List<Message>> messages;
    private MutableLiveData<Boolean> userStatus;

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
        repository.getMessages(chatId).observeForever(messageList -> {
            messages.setValue(messageList);
        });
    }

    public void sendMessage(String chatId, Message message) {
        repository.sendMessage(chatId, message);
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
        repository.deleteMessage(chatId, messageId);
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