package com.example.unimate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unimate.dao.UserDao;
import com.example.unimate.database.AppDatabase;
import com.example.unimate.models.User;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.Executors;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnSignup;
    private TextView tvResponse;
    private RadioGroup rgRole;
    private RadioButton rbStudent, rbStaff;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvResponse = findViewById(R.id.tvResponse);
        rgRole = findViewById(R.id.rgRole);
        rbStudent = findViewById(R.id.rbStudent);
        rbStaff = findViewById(R.id.rbStaff);

        // Initialize Room database
        db = AppDatabase.getInstance(this);

        // Set signup button click listener
        btnSignup.setOnClickListener(v -> signupUser());
    }

    private void signupUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Determine selected role
        int selectedRoleId = rgRole.getCheckedRadioButtonId();
        String role;
        if (selectedRoleId == R.id.rbStudent) {
            role = "student";
        } else if (selectedRoleId == R.id.rbStaff) {
            role = "staff";
        } else {
            showError("Please select a role.");
            return;
        }

        // Validation for Name (Only letters, no numbers)
        if (!name.matches("^[a-zA-Z ]+$")) {
            showError("Use characters only in name.");
            return;
        }

        // Validation for Email (Must end with @gmail.com)
        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            showError("Write a valid email (must be @gmail.com).");
            return;
        }

        // Password should not be empty
        if (password.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        String url = "http://10.184.189.119/StudentAppBackend/api/signup.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("role", role); // Include role in the JSON object
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("SignupDebug", "Failed to connect to server: " + e.getMessage());
                runOnUiThread(() -> showError("Failed to connect to server."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    Log.d("SignupDebug", "Raw response: " + responseString);

                    try {
                        JSONObject jsonResponse = new JSONObject(responseString);
                        String status = jsonResponse.optString("status", "error");

                        runOnUiThread(() -> {
                            if (status.equals("success")) {
                                String userId = jsonResponse.optString("userId", null); // Get userId from response

                                if (userId != null) {
                                    // Save userId and role in SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("userId", userId);
                                    editor.putString("role", role);
                                    editor.apply();

                                    Log.d("SignupDebug", "User ID saved in SharedPreferences: " + userId);
                                } else {
                                    Log.e("SignupDebug", "Error: userId is missing in response!");
                                }

                                // Save user data in Room Database
                                saveUserToDatabase(name, email, role);

                                // Navigate to LoginActivity.java after successful signup
                                Intent intent = new Intent(SignupActivity.this, MainActivity22.class);
                                intent.putExtra("email", email); // Optionally pass email for autofill
                                startActivity(intent);
                                finish();

                            } else {
                                showError(jsonResponse.optString("message", "Signup failed"));
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("SignupDebug", "JSON Parsing error: " + e.getMessage());
                    }
                } else {
                    Log.d("SignupDebug", "Response failed with code: " + response.code());
                }
            }
        });
    }


    private void saveUserToDatabase(String name, String email, String role) {
        Executors.newSingleThreadExecutor().execute(() -> {
            UserDao userDao = db.userDao();
            User user = new User();
            user.name = name;
            user.email = email;
            user.role = role;
            userDao.insertUser(user);
        });
    }
    // Helper method to show error messages
    private void showError(String message) {
        tvResponse.setText(message);
        tvResponse.setTextColor(Color.RED);
        tvResponse.setVisibility(View.VISIBLE);
    }
}
