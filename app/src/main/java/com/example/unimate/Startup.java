package com.example.unimate;

public class Startup {
    private String title;
    private String description;
    private String author;
    private String roles;
    private String contact;

    // Constructor
    public Startup(String title, String description, String author, String roles, String contact) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.roles = roles;
        this.contact = contact;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getRoles() { return roles; }
    public String getContact() { return contact; }
}
