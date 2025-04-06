<?php
include('../config/db_config.php');

$data = json_decode(file_get_contents("php://input"), true);

if (!empty($data['name']) && !empty($data['email']) && !empty($data['password']) && !empty($data['role'])) {
    $name = $data['name'];
    $email = $data['email'];
    $password = password_hash($data['password'], PASSWORD_BCRYPT);
    $role = $data['role']; // Get role from frontend

    if (!in_array($role, ['student', 'staff'])) {
        echo json_encode(["status" => "error", "message" => "Invalid role specified."]);
        exit;
    }

    $checkEmail = $conn->prepare("SELECT id FROM users WHERE email = ?");
    $checkEmail->bind_param("s", $email);
    $checkEmail->execute();
    $checkEmail->store_result();

    if ($checkEmail->num_rows > 0) {
        echo json_encode(["status" => "error", "message" => "Email already registered."]);
    } else {
        $stmt = $conn->prepare("INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)");
        $stmt->bind_param("ssss", $name, $email, $password, $role);

        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Signup successful!"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Failed to signup."]);
        }

        $stmt->close();
    }

    $checkEmail->close();
} else {
    echo json_encode(["status" => "error", "message" => "All fields are required."]);
}

$conn->close();
?>

