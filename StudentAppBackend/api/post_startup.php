<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

// Retrieve POST data
$title = $_POST['title'];
$description = $_POST['description'];
$author = $_POST['author'];
$roles = $_POST['roles'];
$contact = $_POST['contact']; // Ensure Android sends 'Contact' (capital C)

// Connect to database
include("../config/db_config.php");

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Database connection failed"]);
    exit();
}

// Insert into database
$stmt = $conn->prepare("INSERT INTO startups (title, description, author, roles, contact) VALUES (?, ?, ?, ?, ?)");
$stmt->bind_param("sssss", $title, $description, $author, $roles, $contact);

if ($stmt->execute()) {
    http_response_code(201);
    echo json_encode(["status" => "success", "message" => "Startup posted successfully"]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Failed to insert data"]);
}

$stmt->close();
$conn->close();
?>
