package com.example.unimate.models;
// Change package as per your structure

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    public String email;  // Email will be the unique identifier

    public String name;
    public String role;
    public String about;
    public String interests;
    public String education;
    public String profilePhotoUri;
    public String busPassImagePath;
}