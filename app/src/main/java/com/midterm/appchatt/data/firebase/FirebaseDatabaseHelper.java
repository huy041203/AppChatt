package com.midterm.appchatt.data.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midterm.appchatt.model.Message;

public class FirebaseDatabaseHelper {
    private final DatabaseReference database;

    public FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void saveMessage(String chatId, Message message,
                            OnCompleteListener<Void> listener) {
        String messageId = database.child("messages").child(chatId).push().getKey();
        message.setMessageId(messageId);

        database.child("messages").child(chatId).child(messageId)
                .setValue(message)
                .addOnCompleteListener(listener);
    }
}
