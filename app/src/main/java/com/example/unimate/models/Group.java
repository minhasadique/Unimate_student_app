package com.example.unimate.models;


public class Group {
    private String id;
    private String groupName; // Ensure this field exists

    public Group(String id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public String getId() {
        return id;
    }

    public String getGroupName() { // Ensure this method exists
        return groupName;
    }
}
