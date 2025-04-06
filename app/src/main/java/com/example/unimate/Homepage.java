package com.example.unimate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.unimate.R;
import com.example.unimate.models.Post;
import com.example.unimate.adapters.PostAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Homepage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private ProgressBar progressBar;
    private ImageView postaddIcon, chatIcon, searchIcon, profileIcon;

    private static final String URL_POST = "http://10.184.189.119/StudentAppBackend/api/create_post.php";
    private static final String URL_GET_POSTS = "http://10.184.189.119/StudentAppBackend/api/get_posts.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        adapter = new PostAdapter(postList);
        recyclerView.setAdapter(adapter);

        // Navigate to StartupActivity when startup icon is clicked
        ImageView startupIcon = findViewById(R.id.notificationIcon); // Assuming notificationIcon is for startup
        startupIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, StartupActivity.class);
            startActivity(intent);
        });


        // Navigate to ChatActivity when chat icon is clicked
        ImageView chatIcon = findViewById(R.id.chatIcon); // Assuming chatIcon is for startup
        chatIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, ChatActivity.class);
            startActivity(intent);
        });

        // Navigate to SearchActivity when search icon is clicked
        ImageView searchIcon = findViewById(R.id.searchIcon); // Assuming chatIcon is for startup
        searchIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, SearchActivity.class);
            startActivity(intent);
        });

        // Navigate to Profilepage when search icon is clicked
        ImageView profileIcon = findViewById(R.id.profileIcon); // Assuming chatIcon is for startup
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, ProfilePage.class);
            startActivity(intent);
        });

        // Find the TextView
        TextView headerTitle = findViewById(R.id.headerTitle);

        // Create the SpannableString for "UNIMATE"
        SpannableString spannableString = new SpannableString("UNIMATE");

        // Apply color to "UNI"
        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#4282aa")), // Color for "UNI"
                0, // Start index
                3, // End index (exclusive)
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Apply color to "MATE"
        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#23cbc3")), // Color for "MATE"
                3, // Start index
                7, // End index (exclusive)
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Set the styled text to the TextView
        headerTitle.setText(spannableString);

//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        String userRole = sharedPreferences.getString("userRole", ""); // Default to empty if not found
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole", "");
        String email = sharedPreferences.getString("userEmail", "");


        postaddIcon = findViewById(R.id.postaddIcon);
        if (postaddIcon != null) {
            postaddIcon.setOnClickListener(v -> {
                if ("staff".equalsIgnoreCase(userRole)) {
                    showAddPostDialog();
                } else if("student".equalsIgnoreCase(userRole)) {

                    Toast.makeText(Homepage.this, "Only staff members are allowed to add posts.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("Homepage", "postaddIcon not found in layout!");
        }


//        if (postaddIcon != null) {
//            postaddIcon.setOnClickListener(v -> showAddPostDialog());
//        } else {
//            Log.e("HomePage", "addIcon not found in layout!");
//        }

//        ImageView profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(v -> {
//            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
//            String email = sharedPreferences.getString("userEmail", ""); // Fetch stored email

            if (email.equals("admin@gmail.com")) {
                startActivity(new Intent(Homepage.this, AdminActivity.class));
            } else {
                startActivity(new Intent(Homepage.this, ProfilePage.class)); // Normal staff profile
            }
        });


        fetchPosts();
    }

    private void fetchPosts() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_POSTS, null,
                response -> {
                    Log.d("API_RESPONSE", response.toString());
                    try {
                        postList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String author = obj.optString("postAuthor1", "Unknown Author");
                            String content = obj.optString("postText1", "No Content");


                            Post post = new Post(author, content, "just now"); // Replace "just now" with an actual timestamp if available
                            postList.add(post);
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

    private void showAddPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_post, null);
        builder.setView(dialogView);

        EditText authorField = dialogView.findViewById(R.id.etAuthorName);
        EditText contentField = dialogView.findViewById(R.id.etPostContent);
        Button postButton = dialogView.findViewById(R.id.btnAddPost);

        AlertDialog dialog = builder.create();
        dialog.show();

        postButton.setOnClickListener(v -> {
            String author = authorField.getText().toString().trim();
            String content = contentField.getText().toString().trim();

            if (author.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                postButton.setEnabled(false);
                postContent(author, content);
                dialog.dismiss();
            }
        });
    }

    private void postContent(String author, String content) {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST,
                response -> {
                    Log.d("Response", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(getApplicationContext(), "Post added successfully!", Toast.LENGTH_SHORT).show();

                            // 🔹 Create a new Post object
                            Post newPost = new Post(author, content, "Just Now");

                            // 🔹 Ensure postList and postAdapter are NOT null
                            if (postList != null && adapter != null) {
                                postList.add(0, newPost);
                                adapter.notifyItemInserted(0);
                                recyclerView.smoothScrollToPosition(0);
                            } else {
                                Log.e("Debug", "postList or adapter is NULL");
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    if (error.networkResponse != null) {
                        Log.e("VolleyError", "Error: " + new String(error.networkResponse.data));
                    } else {
                        Log.e("VolleyError", "Unknown network error");
                    }
                    progressBar.setVisibility(View.GONE);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("postAuthor1", author);
                params.put("postText1", content);
                params.put("user_id", "1"); // Replace with actual user_id
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


}