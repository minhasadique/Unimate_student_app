<?php
include '../config/db_config.php';

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_FILES['image']) && isset($_POST['email'])) {
    $email = $_POST['email'];
    $imageName = uniqid() . "_" . basename($_FILES['image']['name']);
    $targetPath = "../uploads/" . $imageName;

    if (move_uploaded_file($_FILES['image']['tmp_name'], $targetPath)) {
        $stmt = $conn->prepare("UPDATE users SET bus_pass=? WHERE email=?");
        $stmt->bind_param("ss", $imageName, $email);
        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Bus pass uploaded"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Failed to update database"]);
        }
        $stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Upload failed"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request"]);
}

$conn->close();
?>
