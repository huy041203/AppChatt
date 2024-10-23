package com.midterm.appchatt.model;

import java.util.Hashtable;

public class Message {

    private String messageId;
    private String senderId;
    private String content;
    private long timestamp;
    private String type;  // text/image/file
    private boolean isRead;
    private Hashtable<String, String> reactions;
    /*
        reacting_id: who have reacted the message.
        type: what does they react (like, love, haha, wow, sad, angry)
     */

    public Message() {}

    public Message(String senderId, String content, String type) {
        this.senderId = senderId;
        this.content = content;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
        this.isRead = false;
        this.reactions = new Hashtable<>();
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }


    public Hashtable<String, String> getReactions() {
        return this.reactions;
    }

    public String getReaction(User whoReacted) {
        return this.reactions.get(whoReacted.getUserId());
    }

    public void reactBy(User whoReacting, String type) {
        this.reactions.put(whoReacting.getUserId(), type);
    }

    public void setReactions(Hashtable<String, String> reactions) {
        this.reactions = reactions;
    }

}