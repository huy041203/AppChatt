package com.midterm.appchatt.models;

public class Contact {
    private String avatarUrl;
    private String displayName;

    public Contact(String avatarUrl, String displayName) {
        this.avatarUrl = avatarUrl;
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getDisplayName() {
        return displayName;
    }
}
