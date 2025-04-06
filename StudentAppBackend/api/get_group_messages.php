<?php
require_once "../config/db_config.php";

$group_id = $_GET['group_id'];

$sql = "SELECT group_messages.*, users.name, group_messages.timestamp  
        FROM group_messages 
        JOIN users ON group_messages.sender_id = users.id 
        WHERE group_messages.group_id = '$group_id' 
        ORDER BY group_messages.timestamp ASC";

$result = mysqli_query($conn, $sql);

$messages = [];
while ($row = mysqli_fetch_assoc($result)) {
    $messages[] = $row;
}

echo json_encode(["success" => true, "messages" => $messages]);
?>