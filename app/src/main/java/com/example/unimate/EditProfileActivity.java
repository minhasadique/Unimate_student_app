package com.example.unimate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unimate.dao.UserDao;
import com.example.unimate.database.AppDatabase;
import com.example.unimate.models.User;

import java.util.concurrent.Executors;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // Fixed: Assigned a valid value

    private EditText etName, etAbout, etInterests, etEducation;
    private ImageView ivEditProfilePhoto;
    private Button btnSaveProfile;
    private Uri profileImageUri;

    private AppDatabase db;
    private UserDao userDao;
    private SharedPreferences sharedPreferences;
    private String userEmail; // User's email retrieved from session
    private TextView tvNameError, tvAboutError, tvInterestsError, tvEducationError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        ivEditProfilePhoto = findViewById(R.id.ivEditProfilePhoto);
        etName = findViewById(R.id.etName);
        etAbout = findViewById(R.id.etAbout);
        etInterests = findViewById(R.id.etInterests);
        etEducation = findViewById(R.id.etEducation);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        tvNameError = findViewById(R.id.tvNameError);
        tvAboutError = findViewById(R.id.tvAboutError);
        tvInterestsError = findViewById(R.id.tvInterestsError);
        tvEducationError = findViewById(R.id.tvEducationError);


        // Initialize SharedPreferences to get the logged-in user's email
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", "N/A");

        // Initialize Room database and DAO
        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        // Load existing user data
        loadUserData();

        // Change profile photo action
        ivEditProfilePhoto.setOnClickListener(v -> openFileChooser());

        // Save profile button action
        btnSaveProfile.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getUserByEmail(userEmail);
            if (user != null) {
                runOnUiThread(() -> {
                    etName.setText(user.name);
                    etAbout.setText(user.about);
                    etInterests.setText(user.interests);
                    etEducation.setText(user.education);
                    if (user.profilePhotoUri != null) {
                        profileImageUri = Uri.parse(user.profilePhotoUri);
                        ivEditProfilePhoto.setImageURI(profileImageUri);
                    }
                });
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Photo"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            ivEditProfilePhoto.setImageURI(profileImageUri);
        }
    }

    private void saveUserProfile() {
        String name = etName.getText().toString().trim();
        String about = etAbout.getText().toString().trim();
        String interests = etInterests.getText().toString().trim();
        String education = etEducation.getText().toString().trim();

        // Regex to check if the string contains any numbers
        String numberPattern = ".*\\d+.*";

        if (name.matches(numberPattern)) {
            Toast.makeText(EditProfileActivity.this, "Name should not contain numbers", Toast.LENGTH_SHORT).show();
            return;
        }
        if (about.matches(numberPattern)) {
            Toast.makeText(EditProfileActivity.this, "About section should not contain numbers", Toast.LENGTH_SHORT).show();
            return;
        }
        if (interests.matches(numberPattern)) {
            Toast.makeText(EditProfileActivity.this, "Interests should not contain numbers", Toast.LENGTH_SHORT).show();
            return;
        }
        if (education.matches(numberPattern)) {
            Toast.makeText(EditProfileActivity.this, "Education should not contain numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save or update user object
        Executors.newSingleThreadExecutor().execute(() -> {
            User existingUser = userDao.getUserByEmail(userEmail);
            User user;

            if (existingUser == null) {
                // If user does not exist, create a new one and assign role
                user = new User();
                user.email = userEmail;
                user.role = "User";  // Ensure the role is set
            } else {
                // If user exists, update their existing data
                user = existingUser;
            }

            // Update user details
            user.name = name;
            user.about = about;
            user.interests = interests;
            user.education = education;
            if (profileImageUri != null) {
                user.profilePhotoUri = profileImageUri.toString();
            }

            // Insert or update in Room database
            if (existingUser == null) {
                userDao.insertUser(user); // Insert new user
            } else {
                userDao.updateUser(user); // Update existing user
            }

            runOnUiThread(() -> {
                Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfileActivity.this, ProfilePage.class);
                startActivity(intent);
                finish();
            });
        });
    }

}