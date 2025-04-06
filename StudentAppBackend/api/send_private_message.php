<?php
require_once "../config/db_config.php";

$sender_id = $_POST['sender_id'];
$receiver_id = $_POST['receiver_id'];
$message = $_POST['message'];

$sql = "INSERT INTO private_messages (sender_id, receiver_id, message) VALUES ('$sender_id', '$receiver_id', '$message')";
if (mysqli_query($conn, $sql)) {
    echo json_encode(["success" => true]);
} else {
    echo json_encode(["success" => false]);
}
?>