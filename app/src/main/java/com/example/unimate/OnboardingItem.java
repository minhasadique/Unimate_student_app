package com.example.unimate;

public class OnboardingItem {
    private String title, description;
    private int imageRes;

    public OnboardingItem(String title, String description, int imageRes) {
        this.title = title;
        this.description = description;
        this.imageRes = imageRes;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getImageRes() { return imageRes; }
}
