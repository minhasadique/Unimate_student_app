package com.example.unimate; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private RecyclerView searchResultsRecyclerView;
    private ChipGroup chipGroup;
    private ImageView chatIcon, homeIcon, profileIcon, notificationIcon, settingsIcon;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize views
        initializeViews();
        setupRecyclerView();
        setupSearchFunction();
        setupClickListeners();
    }

    private void initializeViews() {
        // Search related views
        searchEditText = findViewById(R.id.searchEditText);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        chipGroup = findViewById(R.id.chipGroup);

        // Navigation icons
        chatIcon = findViewById(R.id.chatIcon);
        homeIcon = findViewById(R.id.homeIcon);
        profileIcon = findViewById(R.id.profileIcon);
        notificationIcon = findViewById(R.id.notificationIcon);
        settingsIcon = findViewById(R.id.settingsIcon);
    }

    private void setupRecyclerView() {
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter();
        searchResultsRecyclerView.setAdapter(searchAdapter);
    }

    private void setupSearchFunction() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        chatIcon.setOnClickListener(v -> {
            // Navigate to Chat Activity
            startActivity(new Intent(SearchActivity.this, ChatActivity.class));
        });

        homeIcon.setOnClickListener(v -> {
            // Navigate to Home Activity
            startActivity(new Intent(SearchActivity.this, Homepage.class));
            finish();
        });

        // Navigate to StartupActivity when startup icon is clicked
        ImageView startupIcon = findViewById(R.id.notificationIcon); // Assuming notificationIcon is for startup
        startupIcon.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, StartupActivity.class);
            startActivity(intent);
        });

        // Navigate to ProfilePage when profile icon is clicked
        ImageView profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, ProfilePage.class);
            startActivity(intent);
        });

        // Add other click listeners as needed
    }

    private void performSearch(String query) {
        if (query.trim().isEmpty()) {
            searchAdapter.setSearchResults(new ArrayList<>()); // Clear results if empty query
            return;
        }

        String selectedCategory = getSelectedCategory(); // Get the selected category from Chips

        String url = "https://10.184.189.119/StudentAppBackend/api/search.php" + query + "&category=" + selectedCategory;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<SearchItem> results = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            results.add(new SearchItem(
                                    obj.getString("type"),
                                    obj.getString("title"),
                                    obj.getString("description")
                            ));
                        }
                        searchAdapter.setSearchResults(results);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(SearchActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    private List<SearchItem> getDummySearchResults(String query) {
        List<SearchItem> results = new ArrayList<>();
        // Add dummy data
        if (query != null && !query.isEmpty()) {
            results.add(new SearchItem("Student", "John Doe", "Computer Science • 3rd Year"));
            results.add(new SearchItem("Course", "Data Structures", "CS201 • Computer Science"));
            results.add(new SearchItem("Scholarship", "Merit Scholarship 2025", "Amount: $5000"));
        }
        return results;
    }
    private String getSelectedCategory() {
        int selectedChipId = chipGroup.getCheckedChipId();
        if (selectedChipId == R.id.chipStudents) return "students";
        if (selectedChipId == R.id.chipCourses) return "courses";
        if (selectedChipId == R.id.chipScholarships) return "scholarships";
        if (selectedChipId == R.id.chipStartups) return "startups";
        return "all"; // Default category
    }

}