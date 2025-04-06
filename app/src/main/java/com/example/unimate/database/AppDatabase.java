package com.example.unimate.database; // Change package as per your structure

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.unimate.dao.UserDao;
import com.example.unimate.models.User;

@Database(entities = {User.class}, version = 1) // Ensure the correct version here
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "unimate_db")
                    .fallbackToDestructiveMigration() // This will delete old data and create a fresh database
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
}