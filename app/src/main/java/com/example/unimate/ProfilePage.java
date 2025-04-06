package com.example.unimate;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.unimate.database.AppDatabase;
import com.example.unimate.dao.UserDao;
import com.example.unimate.models.User;
import com.example.unimate.database.AppDatabase;
import com.example.unimate.dao.UserDao;
import com.example.unimate.models.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

public class ProfilePage extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView tvName, tvEmail, tvRole, tvAbout, tvInterests, tvEducation;
    private ImageView ivProfilePhoto, ivBusPass, homeIcon;
    private Button btnEditProfile, btnUploadBusPass, btnLogout;
    private AppDatabase db;
    private SharedPreferences sharedPreferences;
    private Uri busPassImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Initialize views
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);
        tvAbout = findViewById(R.id.tvAboutContent);
        tvInterests = findViewById(R.id.tvInterestsContent);
        tvEducation = findViewById(R.id.tvEducationContent);
        ivBusPass = findViewById(R.id.ivBusPass);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnUploadBusPass = findViewById(R.id.btnUploadBusPass);
        btnLogout = findViewById(R.id.btnLogout);
        Button btnPreviewBusPass = findViewById(R.id.btnPreviewBusPass);


        // Initialize Room database
        db = AppDatabase.getInstance(this);

        btnPreviewBusPass.setOnClickListener(v -> showImagePreview());

        // Load user session
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "N/A");
        String role = sharedPreferences.getString("userRole", "N/A");


//        tvName.setText(name);
        tvEmail.setText(email);
        tvRole.setText("Role: " + role);

        // Load user data from the database
        loadUserData(email);

        // Edit profile button action
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Navigate to StartupActivity when startup icon is clicked
        ImageView startupIcon = findViewById(R.id.notificationIcon); // Assuming notificationIcon is for startup
        startupIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, StartupActivity.class);
            startActivity(intent);
        });

        // Navigate to SearchActivity when search icon is clicked
        ImageView searchIcon = findViewById(R.id.searchIcon); // Assuming chatIcon is for startup
        searchIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, SearchActivity.class);
            startActivity(intent);
        });
        homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(v -> {
            // Navigate to Home Activity
            startActivity(new Intent(ProfilePage.this, Homepage.class));
            finish();
        });

        // Upload bus pass button action
        btnUploadBusPass.setOnClickListener(v -> openFileChooser());

        if (ivBusPass.getDrawable() == null) {
            ivBusPass.setVisibility(View.GONE);
        }

        // Logout button action
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadUserData(String email) {
        // Debugging logs
        Log.d("ProfileDebug", "Fetching user data for email: " + email);

        if (email.equals("N/A") || email.isEmpty()) {
            Log.e("ProfileDebug", "Email not found in SharedPreferences");
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().getUserByEmail(email);

            if (user == null) {
                Log.e("ProfileDebug", "No user found with email: " + email);
//                runOnUiThread(() -> {
//                    Toast.makeText(ProfilePage.this, "User not found. Try logging in again!", Toast.LENGTH_SHORT).show();
//                });
                return;
            }

            Log.d("ProfileDebug", "User Retrieved: " + user.email);

            runOnUiThread(() -> {
                tvName.setText(user.name != null ? user.name : "Not Provided");
                tvEmail.setText(user.email != null ? user.email : "Not Provided");
                tvRole.setText(user.role != null ? "Role: " + user.role : "Role: Unknown");
                tvAbout.setText(user.about != null && !user.about.isEmpty() ? user.about : "No about info available.");
                tvInterests.setText(user.interests != null && !user.interests.isEmpty() ? user.interests : "No interests added.");
                tvEducation.setText(user.education != null && !user.education.isEmpty() ? user.education : "No education details available.");
            });
        });
    }


    // Open file chooser to select an image
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            busPassImageUri = data.getData();
            ivBusPass.setImageURI(busPassImageUri);
            ivBusPass.setVisibility(View.VISIBLE); // ✅ Show preview after selecting

            saveBusPassImage(busPassImageUri);
        }
    }


    private void saveBusPassImage(Uri imageUri) {
        String email = sharedPreferences.getString("userEmail", "N/A");

        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().getUserByEmail(email);
            if (user != null) {
                String savedImagePath = saveImageToInternalStorage(imageUri);
                if (savedImagePath != null) {
                    user.busPassImagePath = savedImagePath; // ✅ Assign correct field
                    db.userDao().updateBusPassImage(email, savedImagePath); // ✅ Use correct variables
                }
            }
        });
    }

    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File directory = new File(getFilesDir(), "bus_pass_images");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File newFile = new File(directory, "bus_pass_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(newFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            return newFile.getAbsolutePath();  // Return the saved image path
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void showImagePreview() {
        if (busPassImageUri != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(busPassImageUri);

            builder.setView(imageView);
            builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        } else {
            Toast.makeText(this, "No image uploaded yet!", Toast.LENGTH_SHORT).show();
        }
    }



    // Logout user and clear session
    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(ProfilePage.this, MainActivity22.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}