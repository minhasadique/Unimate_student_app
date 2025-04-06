<?php
require_once "../config/db_config.php";

// Read raw JSON input
$json = file_get_contents("php://input");
$data = json_decode($json, true);

if (!$data || !isset($data['group_id'], $data['sender_id'], $data['message'])) {
    echo json_encode(["success" => false, "error" => "Missing parameters"]);
    exit;
}

$group_id = $data['group_id'];
$sender_id = $data['sender_id'];
$message = $data['message'];

// Insert into database
$sql = "INSERT INTO group_messages (group_id, sender_id, message) VALUES ('$group_id', '$sender_id', '$message')";
if (mysqli_query($conn, $sql)) {
    echo json_encode(["success" => true]);
} else {
    echo json_encode(["success" => false, "error" => mysqli_error($conn)]);
}
?>

