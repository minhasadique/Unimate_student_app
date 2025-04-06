package com.example.unimate; // Replace with your package name

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StartupAdapter adapter;
    private List<Startup> startupList;
    private ProgressBar progressBar;
    private ImageView chatIcon, homeIcon, profileIcon, notificationIcon, settingsIcon, searchIcon;

    private static final String URL_POST_STARTUP = "http://10.184.189.119/StudentAppBackend/api/post_startup.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.startupRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        startupList = new ArrayList<>();
        adapter = new StartupAdapter(startupList);
        recyclerView.setAdapter(adapter);

//        ImageView addIcon = findViewById(R.id.addIcon);
//        if (addIcon != null) {
//            addIcon.setOnClickListener(v -> showAddStartupDialog());
//        } else {
//            Log.e("StartupActivity", "addIcon not found in layout!");
//        }
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String role = sharedPreferences.getString("userRole", ""); // ✅ Use correct key

        ImageView addIcon = findViewById(R.id.addIcon);
        if (addIcon != null) {
            addIcon.setOnClickListener(v -> {
                Toast.makeText(this, "User Role: " + role, Toast.LENGTH_LONG).show();

                if ("student".equalsIgnoreCase(role)) {
                    showAddStartupDialog();
                } else {
                    Toast.makeText(StartupActivity.this, "Only students are allowed to post startup ideas.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("StartupActivity", "addIcon not found in layout!");
        }

        StartupApiService startupApiService = StartupRetrofitClient.getStartupApiService();

        fetchStartups();
        // Navigate to ChatActivity when chat icon is clicked
        ImageView chatIcon = findViewById(R.id.chatIcon); // Assuming chatIcon is for startup
        chatIcon.setOnClickListener(v -> {
            Intent intent = new Intent(StartupActivity.this, ChatActivity.class);
            startActivity(intent);
        });
        // Navigate to Profilepage when search icon is clicked
        ImageView profileIcon = findViewById(R.id.profileIcon); // Assuming chatIcon is for startup
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(StartupActivity.this, ProfilePage.class);
            startActivity(intent);
        });
        homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(v -> {
            // Navigate to Home Activity
            startActivity(new Intent(StartupActivity.this, Homepage.class));
            finish();
        });

    }

    private void fetchStartups() {
        String url = "http://10.184.189.119/StudentAppBackend/api/get_startups.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("API_RESPONSE", response.toString());

                    //       String s[]=response.toString().split("\\{\"status\":\"success\",\"data\":\\[");
                    //   Toast.makeText(this,s[0],Toast.LENGTH_SHORT).show();
                    //Log.d("chumma",s[0]);
                    try {
                        startupList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            String title = obj.optString("title", "No Title");
                            String description = obj.optString("description", "No Description");
                            String author = obj.optString("author", "Unknown Author");
                            String roles = obj.optString("roles", "Unknown Roles");
                            String contact = obj.optString("contact", "No Contact");// ✅ Added contact field
//                            Toast.makeText(this,"hi",Toast.LENGTH_SHORT).show();
                            Startup startup = new Startup(title, description, author, roles, contact);
                            startupList.add(startup);
                        }

                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("PARSE_ERROR", "Error parsing JSON: " + e.getMessage());
                        Toast.makeText(this, "Failed to parse data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("API_ERROR", "Volley error: " + error.toString());
                    Toast.makeText(this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void showAddStartupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_startup, null);
        builder.setView(dialogView);

        EditText titleField = dialogView.findViewById(R.id.dialogStartupTitle);
        EditText descriptionField = dialogView.findViewById(R.id.dialogStartupDescription);
        EditText authorField = dialogView.findViewById(R.id.dialogAuthorName);
        EditText rolesField = dialogView.findViewById(R.id.dialogRolesNeeded);
        EditText contactField = dialogView.findViewById(R.id.dialogContactInfo); // ✅ Added contact field
        Button postButton = dialogView.findViewById(R.id.dialogPostButton);

        AlertDialog dialog = builder.create();
        dialog.show();

        postButton.setOnClickListener(v -> {
            String title = titleField.getText().toString().trim();
            String description = descriptionField.getText().toString().trim();
            String author = authorField.getText().toString().trim();
            String roles = rolesField.getText().toString().trim();
            String contact = contactField.getText().toString().trim(); // ✅ Retrieve contact input
            Toast.makeText(this, "contact: " + contact, Toast.LENGTH_LONG).show();


            if (title.isEmpty() || description.isEmpty() || author.isEmpty() || roles.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                postButton.setEnabled(false); // Prevent multiple clicks
                postStartup(title, description, author, roles, contact);
                dialog.dismiss();
            }
        });
    }



    private void postStartup(String title, String description, String author, String roles, String contact) {
        String url = "http://10.184.189.119/StudentAppBackend/api/post_startup.php";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST_STARTUP,
                response -> {
                    Log.d("Response", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(getApplicationContext(), "Startup posted successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(), "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    Log.e("VolleyError", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("description", description);
                params.put("author", author);
                params.put("roles", roles);
                params.put("contact", contact); // ✅ Send contact field to the backend
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}


