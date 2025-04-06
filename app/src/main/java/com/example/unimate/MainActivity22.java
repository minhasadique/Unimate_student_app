package com.example.unimate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity22 extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvResponse, signupTextView, ForgotPasswordtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);



        if (isLoggedIn) {
            // If already logged in, go to Homepage
            Intent intent = new Intent(MainActivity22.this, Homepage.class);
            startActivity(intent);
            finish();  // Close login activity
            return;
        }

        setContentView(R.layout.activity_main22);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvResponse = findViewById(R.id.tvResponse);
        signupTextView = findViewById(R.id.signupTextView); // Signup text

        btnLogin.setOnClickListener(v -> loginUser());

        // Navigate to SignupActivity when clicking "Signup" text
        signupTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity22.this, SignupActivity.class);
            startActivity(intent);
        });

//         Navigate to SignupActivity when clicking "Signup" text
        ForgotPasswordtv = findViewById(R.id.ForgotPasswordtv);
        ForgotPasswordtv.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity22.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

    }
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            tvResponse.setText("All fields are required.");
            tvResponse.setTextColor(Color.RED);
            tvResponse.setVisibility(View.VISIBLE);
            return;
        }

        String url = "http://10.184.189.119/StudentAppBackend/api/login.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
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
                Log.d("LoginDebug", "Failed to connect to server: " + e.getMessage());
                runOnUiThread(() -> {
                    tvResponse.setText("Failed to connect to server.");
                    tvResponse.setTextColor(Color.RED);
                    tvResponse.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("LoginDebug", "API Response: " + responseBody);

                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String status = jsonResponse.optString("status", "error");
                        String message = jsonResponse.optString("message", "Invalid email or password");

                        if (status.equals("success")) {
                            // Extract user data from JSON response
                            String userId = jsonResponse.optString("user_id", "");  // Ensure your API returns this
                            String userEmail = jsonResponse.optString("email", email); // Fallback to entered email
                            String userRole = jsonResponse.optString("role", ""); // Get user role
                            Log.d("LoginDebug", "Full API Response: " + responseBody);


                            runOnUiThread(() -> {
                                // Save user details in SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putString("user_id", userId);  // Save user ID
                                editor.putString("email", userEmail);  // Save email for reference
                                editor.putString("userRole", userRole); // Save role correctly
                                editor.apply();

                                // Debug log
                                Log.d("LoginDebug", "Saved to SharedPreferences - user_id: " + userId + ", email: " + userEmail + ", role: " + userRole);

                                // Show success message and navigate to Homepage
                                tvResponse.setTextColor(Color.GREEN);
                                tvResponse.setText("Login successful!");
                                Intent intent = new Intent(MainActivity22.this, Homepage.class);
                                startActivity(intent);
                                finish();
                            });
                        } else {
                            runOnUiThread(() -> {
                                // Show error message for invalid credentials
                                tvResponse.setTextColor(Color.RED);
                                tvResponse.setText("Invalid email or password.");
                                tvResponse.setVisibility(View.VISIBLE);
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("LoginDebug", "JSON Parsing error: " + e.getMessage());
                    }
                } else {
                    runOnUiThread(() -> {
                        tvResponse.setTextColor(Color.RED);
                        tvResponse.setText("Invalid email or password. Please try again!");
                        tvResponse.setVisibility(View.VISIBLE);
                    });
                }
            }
        });
    }
}