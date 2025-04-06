<?php
require_once "../config/db_config.php";

// Get JSON input
$data = json_decode(file_get_contents("php://input"), true);

// Check if group_name is provided
if (!isset($data['group_name']) || empty($data['group_name'])) {
    echo json_encode(["success" => false, "message" => "Group name is required"]);
    exit();
}

$group_name = $data['group_name'];

// Insert into database
$sql = "INSERT INTO groups (group_name) VALUES (?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $group_name);

if ($stmt->execute()) {
    echo json_encode(["success" => true, "message" => "Group created successfully"]);
} else {
    echo json_encode(["success" => false, "message" => "Failed to create group"]);
}

$stmt->close();
$conn->close();
?>
