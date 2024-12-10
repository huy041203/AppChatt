package com.midterm.appchatt.model;

public class Contact {
    private String userId;
    private String contactId;
    private String displayName;
    private String avatarUrl;

    public Contact(String userId, String contactId, String displayName, String avatarUrl) {
        this.userId = userId;
        this.contactId = contactId;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
    }
    public Contact() {
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
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
}