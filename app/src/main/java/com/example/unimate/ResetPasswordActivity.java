package com.example.unimate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText etOTP, etNewPassword;
    Button btnResetPassword;
    TextView tvResponse;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etOTP = findViewById(R.id.etOTP);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvResponse = findViewById(R.id.tvResponse);

        // Get email from previous activity
        userEmail = getIntent().getStringExtra("email");

        btnResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String otp = etOTP.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        if (otp.isEmpty() || newPassword.isEmpty()) {
            tvResponse.setText("All fields are required.");
            tvResponse.setTextColor(Color.RED);
            tvResponse.setVisibility(View.VISIBLE);
            return;
        }

        String url = "http://10.184.189.119/StudentAppBackend/api/reset_password.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", userEmail);
            jsonObject.put("otp", otp);
            jsonObject.put("new_password", newPassword);
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
                runOnUiThread(() -> {
                    tvResponse.setText("Failed to connect to server.");
                    tvResponse.setTextColor(Color.RED);
                    tvResponse.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseData = response.body().string();
                    System.out.println("Server Response:" +responseData);

                    // Check if response is valid JSON
                    if (responseData.trim().startsWith("{")) {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        runOnUiThread(() -> {
                            if (status.equals("success")) {
                                Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                                finish(); // Close the activity after successful reset
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        runOnUiThread(() -> Toast.makeText(ResetPasswordActivity.this, "Invalid server response", Toast.LENGTH_LONG).show());
                    }

                } catch (Exception e) {
                    Log.e("ResetPasswordDebug", "Error parsing response", e);
                    runOnUiThread(() -> Toast.makeText(ResetPasswordActivity.this, "Unexpected server response", Toast.LENGTH_LONG).show());
                }
            }
        });
    }
}