package com.example.unimate.models;

public class Post {
    private String postAuthor1;
    private String postText1;
    private String created_at; // Matches "timestamp"

    // Constructor (Updated)
    public Post(String postAuthor1, String postText1, String created_at) {
        this.postAuthor1 = postAuthor1;
        this.postText1 = postText1;
        this.created_at = created_at;
    }

    public String getAuthor() {
        return postAuthor1;
    }

    public String getContent() {
        return postText1;
    }

    public String getTimestamp() {
        return created_at;
    }
}
