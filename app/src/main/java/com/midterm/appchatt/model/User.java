package com.midterm.appchatt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String userId;
    private String email;
    private String displayName;
    private String avatarUrl;
    private String status;  // online/offline
    private long lastActive;


    // Constructor mặc định cho Firebase
    public User() {}

    public User(String userId, String email, String displayName) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.status = "offline";
        this.avatarUrl = "data:image/jpeg;base64,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";
        this.lastActive = System.currentTimeMillis();
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getLastActive() {
        return lastActive;
    }

    public void setLastActive(long lastActive) {
        this.lastActive = lastActive;
    }


    public String getName() {
    return  displayName;}
    protected User(Parcel in) {
        userId = in.readString();
        email = in.readString();
        displayName = in.readString();
        avatarUrl = in.readString();
        status = in.readString();
        lastActive = in.readLong();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(email);
        dest.writeString(displayName);
        dest.writeString(avatarUrl);
        dest.writeString(status);
        dest.writeLong(lastActive);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
