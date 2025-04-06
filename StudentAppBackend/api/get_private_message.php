<?php
require_once "../config/db_config.php";

$sender_id = $_GET['sender_id'];
$receiver_id = $_GET['receiver_id'];

$sql = "SELECT * FROM private_messages 
        WHERE (sender_id = '$sender_id' AND receiver_id = '$receiver_id') 
        OR (sender_id = '$receiver_id' AND receiver_id = '$sender_id') 
        ORDER BY sent_at ASC";
$result = mysqli_query($conn, $sql);

$messages = [];
while ($row = mysqli_fetch_assoc($result)) {
    $messages[] = $row;
}

echo json_encode(["success" => true, "messages" => $messages]);
?>