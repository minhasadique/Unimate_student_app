package com.example.unimate;

import android.content.Intent;
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
import okhttp3.*;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnRequestOTP;
    TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI components
        etEmail = findViewById(R.id.etEmail);
        btnRequestOTP = findViewById(R.id.btnRequestOTP);
        tvResponse = findViewById(R.id.tvResponse);
        tvResponse.setVisibility(View.GONE);

        // Click listener for the Request OTP button
        btnRequestOTP.setOnClickListener(v -> {
            Log.d("ForgotPasswordDebug", "Request OTP button clicked"); // Log button click
            sendResetRequest();
        });
    }

    private void sendResetRequest() {
        Log.d("ForgotPasswordDebug", "sendResetRequest() called");

        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Log.d("ForgotPasswordDebug", "Email field is empty");
            runOnUiThread(() -> {
                tvResponse.setText("Please enter your email.");
                tvResponse.setTextColor(Color.RED);
                tvResponse.setVisibility(View.VISIBLE);
            });
            return;
        }

        Log.d("ForgotPasswordDebug", "Email entered: " + email);

        // Backend API URL
        String url = "http://10.184.189.119/StudentAppBackend/api/forgot_password.php";

        // Create JSON object for request
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
        } catch (JSONException e) {
            Log.e("ForgotPasswordDebug", "JSON Exception: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        // Build HTTP request
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Log.d("ForgotPasswordDebug", "Request prepared, sending...");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ForgotPasswordDebug", "Failed to connect: " + e.getMessage());
                runOnUiThread(() -> {
                    tvResponse.setText("Failed to connect. Check your internet.");
                    tvResponse.setTextColor(Color.RED);
                    tvResponse.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("ForgotPasswordDebug", "Response received, status code: " + response.code());

                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("ForgotPasswordDebug", "Response Body: " + responseBody);

                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        runOnUiThread(() -> {
                            tvResponse.setText(message);
                            tvResponse.setVisibility(View.VISIBLE);

                            if (status.equals("success")) {
                                tvResponse.setTextColor(Color.GREEN);

                                // Redirect to ResetPasswordActivity
                                Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            } else {
                                tvResponse.setTextColor(Color.RED);
                            }
                        });

                    } catch (JSONException e) {
                        Log.e("ForgotPasswordDebug", "JSON Parsing Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ForgotPasswordDebug", "Server error: " + response.code());
                    runOnUiThread(() -> {
                        tvResponse.setText("Server error. Try again later.");
                        tvResponse.setTextColor(Color.RED);
                        tvResponse.setVisibility(View.VISIBLE);
                    });
                }
            }
        });
    }
}