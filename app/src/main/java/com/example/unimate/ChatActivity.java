package com.example.unimate;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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



public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerUsers, recyclerGroups;
    List<ChatUser> userList = new ArrayList<>();
    List<Group> groupList = new ArrayList<>();
    UserAdapter userAdapter;
    GroupAdapter groupAdapter;
    String currentUserId;
    TextView textGroupChats;
    ImageView backIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerUsers = findViewById(R.id.recyclerUsers);
        recyclerGroups = findViewById(R.id.recyclerGroups);
        textGroupChats = findViewById(R.id.textGroupChats);
        backIcon = findViewById(R.id.backIcon);
        FloatingActionButton fabCreateGroup = findViewById(R.id.fab_create_group);

        // Set up RecyclerView for Users
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerUsers.setNestedScrollingEnabled(true);
        recyclerUsers.setHasFixedSize(true);

        // Set up RecyclerView for Groups
        recyclerGroups.setLayoutManager(new LinearLayoutManager(this));
        recyclerGroups.setNestedScrollingEnabled(true);
        recyclerGroups.setHasFixedSize(true);

        userAdapter = new UserAdapter(this, userList);
        groupAdapter = new GroupAdapter(this, groupList);

        recyclerUsers.setAdapter(userAdapter);
        recyclerGroups.setAdapter(groupAdapter);

        // Get Current User ID
        currentUserId = getSharedPreferences("UserSession", MODE_PRIVATE).getString("user_id", null);
        if (currentUserId == null) {
            Log.e("ChatActivity", "Error: Current User ID is NULL");
        } else {
            Log.d("ChatActivity", "Current User ID: " + currentUserId);
        }

        // Click to open private chat
        userAdapter.setOnItemClickListener(position -> {
            ChatUser selectedUser = userList.get(position);
            Log.d("ChatActivity", "Opening ChatScreenActivity - User: " + selectedUser.getName());

            Intent intent = new Intent(ChatActivity.this, ChatScreenActivity.class);
            intent.putExtra("user_id", currentUserId);
            intent.putExtra("chatUserId", selectedUser.getId());
            intent.putExtra("user_name", selectedUser.getName());
            startActivity(intent);
        });

        // Navigate to ChatActivity when chat icon is clicked
        ImageView backIcon = findViewById(R.id.backIcon); // Assuming chatIcon is for startup
        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, Homepage.class);
            startActivity(intent);
        });

        // Click to open group chat
        groupAdapter.setOnItemClickListener(position -> {
            Group selectedGroup = groupList.get(position);

            if (selectedGroup == null || selectedGroup.getId() == null || currentUserId == null) {
                Toast.makeText(this, "Error: Missing group or user data", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("ChatActivity", "Opening GroupChatActivity - Group: " + selectedGroup.getGroupName());

            Intent intent = new Intent(ChatActivity.this, GroupChatActivity.class);
            intent.putExtra("user_id", currentUserId);
            intent.putExtra("group_id", selectedGroup.getId());
            intent.putExtra("group_name", selectedGroup.getGroupName());
            startActivity(intent);
        });

        // Floating Action Button to create a group
        fabCreateGroup.setOnClickListener(v -> showCreateGroupDialog());

        // Fetch groups & users
        fetchGroups();
        fetchUsers();
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
                                        userObj.getString("role")
                                );
                                if (!user.getId().equals(currentUserId)) {
                                    userList.add(user);
                                }
                            }
                            runOnUiThread(() -> userAdapter.notifyDataSetChanged()); // Ensure UI updates properly
                        } else {
                            Log.e("ChatActivity", "No users found!");
                        }
                    } catch (JSONException e) {
                        Log.e("ChatActivity", "JSON Parsing Error: " + e.getMessage());
                    }
                },
                error -> Log.e("ChatActivity", "Volley Error: " + error.getMessage()));

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchGroups() {
        String url = "http://10.184.189.119/StudentAppBackend/api/get_groups.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONArray groupsArray = response.getJSONArray("groups");
                            groupList.clear();
                            for (int i = 0; i < groupsArray.length(); i++) {
                                JSONObject groupObj = groupsArray.getJSONObject(i);
                                Group group = new Group(groupObj.getString("id"), groupObj.getString("group_name"));
                                groupList.add(group);
                            }
                            runOnUiThread(() -> groupAdapter.notifyDataSetChanged()); // Ensure UI updates properly
                        } else {
                            Log.e("ChatActivity", "No groups found!");
                        }
                    } catch (JSONException e) {
                        Log.e("ChatActivity", "JSON Parsing Error: " + e.getMessage());
                    }
                },
                error -> Log.e("ChatActivity", "Volley Error: " + error.getMessage()));

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }







    private void showCreateGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Group");

        final EditText input = new EditText(this);
        input.setHint("Enter group name");
        builder.setView(input);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String groupName = input.getText().toString().trim();
            if (!groupName.isEmpty()) {
                createNewGroup(groupName);
            } else {
                Toast.makeText(this, "Group name cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void createNewGroup(String groupName) {
        String CREATE_GROUP_API = "http://10.184.189.119/StudentAppBackend/api/create_group.php";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("group_name", groupName);
//            requestBody.put("created_by", senderId); // Current user as the creator
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CREATE_GROUP_API, requestBody,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(this, "Group created successfully!", Toast.LENGTH_SHORT).show();
                            fetchGroups(); // Refresh groups after creation
                        } else {
                            Toast.makeText(this, "Failed to create group", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("ChatActivity", "Group Creation Error: " + error.toString());
                    Toast.makeText(this, "Error creating group", Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}



