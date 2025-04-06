package com.example.unimate;


public class SearchItem {
    private String type;
    private String title;
    private String subtitle;

    public SearchItem(String type, String title, String subtitle) {
        this.type = type;
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
}
