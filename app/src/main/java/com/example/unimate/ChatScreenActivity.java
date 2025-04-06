package com.example.unimate;

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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.unimate.adapters.MessageAdapter;
import com.example.unimate.models.Message;
import com.example.unimate.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatScreenActivity extends AppCompatActivity {
    private ListView listView;
    private EditText messageInput;
    private Button sendButton;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private String userId, userName, senderId, receiverId;
    private TextView chatTitle; // Make sure you have a TextView in your layout to display the chat title

    private static final String CHAT_API_URL = "http://10.184.189.119/StudentAppBackend/api/get_messages.php"; // Update with your server URL
    private static final String SEND_MESSAGE_API = "http://10.184.189.119/StudentAppBackend/api/send_message.php"; // Update with your server URL

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        listView = findViewById(R.id.listView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatTitle = findViewById(R.id.chatTitle);

        messageList = new ArrayList<>();

        // Get IDs from Intent
        senderId = getIntent().getStringExtra("user_id");  // Current User ID
        receiverId = getIntent().getStringExtra("chatUserId");  // Chat Partner ID
        userName = getIntent().getStringExtra("user_name");  // Chat Partner Name

        Log.d("ChatScreenActivity", "Intent Data - senderId: " + senderId + ", receiverId: " + receiverId);

        if (senderId == null || receiverId == null) {
            Toast.makeText(this, "Error: User data missing!", Toast.LENGTH_LONG).show();
            Log.e("ChatScreenActivity", "User data missing! senderId: " + senderId + ", receiverId: " + receiverId);
            finish();  // Exit the activity if IDs are missing
            return;
        }

        if (userName != null) {
            chatTitle.setText("Chat with " + userName);
        }

        messageAdapter = new MessageAdapter(this, messageList, senderId);
        listView.setAdapter(messageAdapter);

        loadMessages();  // Load chat messages
        sendButton.setOnClickListener(v -> sendMessage());
    }



    private void loadMessages() {
        String chatUserId = getIntent().getStringExtra("chatUserId");
        String url = CHAT_API_URL + "?userId=" + senderId + "&chatUserId=" + chatUserId;


        Log.d("ChatScreenActivity", "Fetching messages from: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("ChatScreenActivity", "Response: " + response.toString()); // 🔍 Log response

                    messageList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject msg = response.getJSONObject(i);

                            String senderId = msg.getString("sender_id");
                            String receiverId = msg.getString("receiver_id");
                            String text = msg.getString("message");
                            String timestamp = msg.getString("timestamp");


                            boolean isSent = senderId.equals(this.senderId);

                            messageList.add(new Message(senderId, receiverId, text, timestamp, isSent));
                        }

                        messageAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("ChatScreenActivity", "JSON Error: " + e.getMessage()); // 🔴 Log JSON error
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing messages", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("ChatScreenActivity", "Volley Error: " + error.toString()); // 🔴 Log Volley error
                    Toast.makeText(this, "Failed to fetch messages", Toast.LENGTH_SHORT).show();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }



    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        if (senderId == null || receiverId == null) {
            Log.e("ChatScreenActivity", "Error: senderId or receiverId is null");
            Toast.makeText(this, "User data missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("sender_id", senderId);
            requestBody.put("receiver_id", receiverId);
            requestBody.put("message", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("ChatScreenActivity", "Sending message: " + requestBody.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SEND_MESSAGE_API, requestBody,
                response -> {
                    Log.d("ChatScreenActivity", "Message Sent Response: " + response.toString());

                    try {
                        if (response.getBoolean("success")) {
                            JSONObject responseData = response.getJSONObject("data");

                            String sender = responseData.getString("sender_id");
                            String receiver = responseData.getString("receiver_id");
                            String messageText = responseData.getString("message");
                            String timestamp = responseData.getString("timestamp");

                            boolean isSent = sender.equals(senderId);
                            loadMessages();

                            messageInput.setText(""); // Clear input after sending
                        } else {
                            Toast.makeText(this, "Message sending failed!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("ChatScreenActivity", "Message Send Error: " + error.toString());
                    Toast.makeText(this, "Message sending failed!", Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

}
