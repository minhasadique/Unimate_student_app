package com.example.unimate;

public class MessageModel {
    private int senderId, receiverId;
    private String message, timestamp;

    public MessageModel(int senderId, int receiverId, String message, String timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }
}
