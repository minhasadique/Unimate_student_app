package com.example.unimate.models;

public class Message {
    private String senderId;
    private String receiverId;
    private String message;
    private String timestamp;
    private boolean isSent;

    public Message(String senderId, String receiverId, String message, String timestamp, boolean isSent) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
        this.isSent = isSent;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message; // Fix: Use getMessage() instead of getText()
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isSent() {
        return isSent;
    }
}


