<?php
include '../config/db_config.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents("php://input"), true);
    
    if (isset($data['sender_id']) && isset($data['receiver_id']) && isset($data['message'])) {
        $sender_id = $data['sender_id'];
        $receiver_id = $data['receiver_id'];
        $message = $data['message'];
        $timestamp = date("Y-m-d H:i:s");

        $stmt = $conn->prepare("INSERT INTO messages (sender_id, receiver_id, message, timestamp) VALUES (?, ?, ?, ?)");
        $stmt->bind_param("ssss", $sender_id, $receiver_id, $message, $timestamp);

        if ($stmt->execute()) {
            $response['success'] = true;
            $response['data'] = array(
                'sender_id' => $sender_id,
                'receiver_id' => $receiver_id,
                'message' => $message,
                'timestamp' => $timestamp
            );
        } else {
            $response['success'] = false;
            $response['error'] = "Database error: " . $conn->error;
        }

        $stmt->close();
    } else {
        $response['success'] = false;
        $response['error'] = "Missing parameters";
    }
}

echo json_encode($response);
?>

