package com.midterm.appchatt.model;

public class Chat {
    private String avatarUrl;
    private String displayName;
    private String lastMessageContent;
    private String time;

    public Chat(String avatarUrl, String displayName, String lastMessageContent, String time) {
        this.avatarUrl = avatarUrl;
        this.displayName = displayName;
        this.lastMessageContent = lastMessageContent;
        this.time = time;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public String getTime() {
        return time;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
