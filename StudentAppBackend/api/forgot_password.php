<?php
header("Content-Type: application/json");
error_reporting(E_ALL);
ini_set('display_errors', 1);

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
require '../config/db_config.php'; // Ensure the correct path to database configuration
require 'vendor/autoload.php'; // Ensure PHPMailer is installed

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $json = file_get_contents('php://input');
    $data = json_decode($json, true);

    if (!isset($data["email"]) || empty($data["email"])) {
        echo json_encode(["status" => "error", "message" => "Email is required"]);
        exit;
    }

    $email = $data["email"];

    // Check if the email exists in the database
    $stmt = $conn->prepare("SELECT id FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows === 0) {
        echo json_encode(["status" => "error", "message" => "Email not found"]);
        exit;
    }

    // Generate OTP and expiry time
    $otp = rand(100000, 999999);
    $expiry = time() + (10 * 60); // OTP valid for 10 minutes

    // Store OTP in the database
    $stmt = $conn->prepare("UPDATE users SET otp_code = ?, otp_expiry = ? WHERE email = ?");
    $stmt->bind_param("sis", $otp, $expiry, $email);
    if (!$stmt->execute()) {
        echo json_encode(["status" => "error", "message" => "Failed to save OTP"]);
        exit;
    }

    // Send OTP via Email
    $mail = new PHPMailer(true);
    try {
        $mail->isSMTP();
        $mail->Host       = 'smtp.gmail.com';
        $mail->SMTPAuth   = true;
        $mail->Username   = 'minhasadique424@gmail.com'; // Your Gmail
        $mail->Password   = 'vkpc blgk psru taoo'; // Your Gmail App Password
        $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
        $mail->Port       = 587;

        $mail->setFrom('your_email@gmail.com', 'Unimate App');
        $mail->addAddress($email);

        $mail->Subject = "Your OTP Code";
        $mail->Body    = "Your OTP is: " . $otp . ". This OTP is valid for 10 minutes.";

        if ($mail->send()) {
            echo json_encode(["status" => "success", "message" => "OTP sent successfully"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Failed to send OTP"]);
        }
    } catch (Exception $e) {
        echo json_encode(["status" => "error", "message" => "Mailer Error: " . $mail->ErrorInfo]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}
?>


    
