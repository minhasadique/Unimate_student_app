<?php
require '../config/db_config.php';

header("Content-Type: application/json");
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Log the API call
error_log("Reset Password API called");

// Read JSON input
$data = json_decode(file_get_contents("php://input"), true);

// Check if JSON decoding failed
if (!$data) {
    error_log("JSON decoding failed: " . json_last_error_msg());
    echo json_encode(["status" => "error", "message" => "Invalid JSON format"]);
    exit;
}

// Extract data
$email = $data['email'] ?? '';
$otp = $data['otp'] ?? '';
$newPassword = $data['new_password'] ?? '';

// Log received request
error_log("Received email: $email, OTP: $otp, New Password: [hidden]");

// Check for missing fields
if (empty($email) || empty($otp) || empty($newPassword)) {
    echo json_encode(["status" => "error", "message" => "All fields are required"]);
    exit;
}

// Check if OTP is valid
$stmt = $conn->prepare("SELECT otp_code, otp_expiry FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows == 0) {
    echo json_encode(["status" => "error", "message" => "Invalid email"]);
    exit;
}

$row = $result->fetch_assoc();
if ($row['otp_code'] != $otp || time() > $row['otp_expiry']) {
    echo json_encode(["status" => "error", "message" => "Invalid or expired OTP"]);
    exit;
}

// Hash new password and update
$hashedPassword = password_hash($newPassword, PASSWORD_DEFAULT);
$stmt = $conn->prepare("UPDATE users SET password = ?, otp_code = NULL, otp_expiry = NULL WHERE email = ?");
$stmt->bind_param("ss", $hashedPassword, $email);
$stmt->execute();

echo json_encode(["status" => "success", "message" => "Password updated successfully"]);
?>
