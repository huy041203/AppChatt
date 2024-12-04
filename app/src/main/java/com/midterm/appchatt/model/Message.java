package com.midterm.appchatt.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@IgnoreExtraProperties
public class Message implements Parcelable {
    private String messageId;
    private String senderId;
    private String content;
    private String type;
    private Long timestamp;

    // Constructor mặc định cho Firebase
    public Message() {
        this.timestamp = System.currentTimeMillis();
    }

    public Message(String senderId, String content, String type) {
        this.senderId = senderId;
        this.content = content;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    // Convert to Map for Firebase
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("senderId", senderId);
        result.put("content", content);
        result.put("type", type);
        result.put("timestamp", timestamp);
        return result;
    }

    // Parcelable implementation
    protected Message(Parcel in) {
        senderId = in.readString();
        content = in.readString();
        type = in.readString();
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(senderId);
        dest.writeString(content);
        dest.writeString(type);
        dest.writeLong(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    // For comparison in RecyclerView adapter
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return senderId != null && senderId.equals(message.senderId);
    }

    @Override
    public int hashCode() {
        return senderId != null ? senderId.hashCode() : 0;
    }

    // For debugging
    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "senderId='" + senderId + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}