<?php
include '../config/db_config.php'; // Adjust path if needed

header('Content-Type: application/json');

// Get request data
$data = json_decode(file_get_contents("php://input"), true);

if (!empty($data['email']) && !empty($data['password'])) {
    $email = trim($data['email']);
    $password = trim($data['password']);

    // Validate email format
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        http_response_code(400);
        echo json_encode(["status" => "error", "message" => "Invalid email format"]);
        exit;
    }

    // Use prepared statements for security
    $stmt = $conn->prepare("SELECT id, name, email, password, role FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();
    $user = $result->fetch_assoc();
    
    if ($user) {
        if (password_verify($password, $user['password'])) {
            http_response_code(200); // Success response
            echo json_encode([
                "status" => "success",
                "message" => "Login successful",
                "user_id" => $user['id'],
                "name" => $user['name'],
                "email" => $user['email'],
                "role" => $user['role'] // Send role (student or staff)
            ]);
            exit;
        } else {
            http_response_code(401); // Unauthorized
            echo json_encode(["status" => "error", "message" => "Invalid credentials"]);
            exit;
        }
    } else {
        http_response_code(404); // Not found
        echo json_encode(["status" => "error", "message" => "User not found"]);
        exit;
    }
    
    $stmt->close();
} else {
    http_response_code(400); // Bad request
    echo json_encode(["status" => "error", "message" => "All fields are required"]);
    exit;
}

$conn->close();
?>
