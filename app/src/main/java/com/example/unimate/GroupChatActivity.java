package com.example.unimate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.unimate.adapters.MessageAdapter;
import com.example.unimate.models.Message;
import com.example.unimate.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {
    private ListView listView;
    private EditText messageInput;
    private Button sendButton;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private String groupId, groupName, chatUserId;
    private TextView chatTitle;

    private static final String GROUP_CHAT_API_URL = "http://10.184.189.119/StudentAppBackend/api/get_group_messages.php";
    private static final String SEND_GROUP_MESSAGE_API = "http://10.184.189.119/StudentAppBackend/api/send_group_message.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        listView = findViewById(R.id.listView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatTitle = findViewById(R.id.chatTitle);

        messageList = new ArrayList<>();

        // Retrieve intent data
        Intent intent = getIntent();
        groupId = intent.getStringExtra("group_id");  // FIXED key name
        groupName = intent.getStringExtra("group_name");  // FIXED key name
        chatUserId = intent.getStringExtra("user_id");

        if (groupId == null || chatUserId == null) {
            Toast.makeText(this, "Error: Missing group or user data!", Toast.LENGTH_LONG).show();
            Log.e("GroupChatActivity", "Error: Missing groupId or userId! groupId=" + groupId + ", userId=" + chatUserId);
            finish();
            return;
        }

        // Log after retrieving data
        Log.d("GroupChatActivity", "Received groupId: " + groupId + ", groupName: " + groupName + ", userId: " + chatUserId);

        // Set group name as chat title
        if (groupName != null) {
            chatTitle.setText("Group: " + groupName);
        } else {
            chatTitle.setText("Group Chat");
        }

        messageAdapter = new MessageAdapter(this, messageList, chatUserId);
        listView.setAdapter(messageAdapter);

        loadMessages();

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        String url = GROUP_CHAT_API_URL + "?group_id=" + groupId;

        Log.d("GroupChatActivity", "Fetching messages from: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("GroupChatActivity", "Response: " + response.toString());

                    messageList.clear();
                    try {
                        JSONArray messages = response.getJSONArray("messages");
                        for (int i = 0; i < messages.length(); i++) {
                            JSONObject msg = messages.getJSONObject(i);
                            String senderId = msg.getString("sender_id");
                            String senderName = msg.getString("name");
                            String message = msg.getString("message");
                            String timestamp = msg.optString("timestamp", "");

                            boolean isSent = senderId.equals(chatUserId);
                            messageList.add(new Message(senderId, senderName, message, timestamp, isSent));
                        }
                        messageAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("GroupChatActivity", "JSON Parsing Error: " + e.getMessage());
                        Toast.makeText(this, "Error parsing messages", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("GroupChatActivity", "Volley Error: " + error.toString());
                    Toast.makeText(this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("GroupChatActivity", "group_id: " + groupId + ", sender_id: " + chatUserId + ", message: " + text);


        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("group_id", groupId);
            requestBody.put("sender_id", chatUserId);  // Make sure chatUserId is correct
            requestBody.put("message", text);
        } catch (JSONException e) {
            Log.e("GroupChatActivity", "JSON Exception: " + e.getMessage());
            return;
        }
        if (groupId == null || chatUserId == null || text.isEmpty()) {
            Toast.makeText(this, "Error: Missing group ID, user ID, or message!", Toast.LENGTH_LONG).show();
            Log.e("GroupChatActivity", "Missing parameters - groupId: " + groupId + ", chatUserId: " + chatUserId + ", message: " + text);
            return;
        }


        Log.d("GroupChatActivity", "Sending message: " + requestBody.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SEND_GROUP_MESSAGE_API, requestBody,
                response -> {
                    Log.d("GroupChatActivity", "Message Sent Response: " + response.toString());
                    try {
                        if (response.getBoolean("success")) {
                            loadMessages();  // Refresh chat
                            messageInput.setText(""); // Clear input field
                        } else {
                            String errorMsg = response.has("error") ? response.getString("error") : "Unknown error";
                            Toast.makeText(this, "Message sending failed! " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("GroupChatActivity", "JSON Parsing Error: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("GroupChatActivity", "Volley Error: " + error.toString());
                    Toast.makeText(this, "Network error. Check your connection.", Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

}
