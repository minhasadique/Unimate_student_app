package com.example.unimate;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unimate.adapters.UserAdapter;
import com.example.unimate.models.ChatUser;
import com.example.unimate.utils.VolleySingleton;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.unimate.adapters.UserAdapter;
import com.example.unimate.adapters.GroupAdapter;
import com.example.unimate.models.ChatUser;
import com.example.unimate.models.Group;
import com.example.unimate.models.ChatUser;
import com.example.unimate.models.User;
import com.example.unimate.utils.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    private SharedPreferences sharedPreferences;

    List<ChatUser> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);



        fetchUsers(); // Call the function to fetch users
    }

    private void fetchUsers() {
        String url = "http://10.184.189.119/StudentAppBackend/api/get_users.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONArray usersArray = response.getJSONArray("users");
                            userList.clear();
                            for (int i = 0; i < usersArray.length(); i++) {
                                JSONObject userObj = usersArray.getJSONObject(i);
                                ChatUser user = new ChatUser(
                                        userObj.getString("id"),
                                        userObj.getString("name"),
                                        userObj.getString("role") // Fetch role
                                );
                                userList.add(user);
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("AdminActivity", "Error: " + error.getMessage()));

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    // Logout user and clear session
    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(AdminActivity.this, MainActivity22.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
