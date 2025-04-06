<?php
require_once '../config/db_config.php'; // Adjust path if needed

header("Content-Type: application/json");

if (isset($_GET['userId']) && isset($_GET['chatUserId'])) {
    $userId = $_GET['userId'];
    $chatUserId = $_GET['chatUserId'];

    $stmt = $conn->prepare("SELECT * FROM messages 
        WHERE (sender_id = ? AND receiver_id = ?) 
           OR (sender_id = ? AND receiver_id = ?) 
        ORDER BY timestamp ASC");
    $stmt->bind_param("ssss", $userId, $chatUserId, $chatUserId, $userId);
    
    $stmt->execute();
    $result = $stmt->get_result();

    $messages = array();
    while ($row = $result->fetch_assoc()) {
        $messages[] = $row;
    }

    echo json_encode($messages);
} else {
    echo json_encode(["error" => "Missing parameters"]);
}
?>

