<?php
include 'config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $group_id = $_POST['group_id'];
    $user_id = $_POST['user_id'];

    // Check if the user is already in the group
    $check_stmt = $conn->prepare("SELECT * FROM group_members WHERE group_id = ? AND user_id = ?");
    $check_stmt->bind_param("ii", $group_id, $user_id);
    $check_stmt->execute();
    $result = $check_stmt->get_result();

    if ($result->num_rows > 0) {
        echo json_encode(["status" => "error", "message" => "User already in the group"]);
    } else {
        // Add user to group
        $stmt = $conn->prepare("INSERT INTO group_members (group_id, user_id) VALUES (?, ?)");
        $stmt->bind_param("ii", $group_id, $user_id);
        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "User joined the group"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Failed to join group"]);
        }
        $stmt->close();
    }
    $check_stmt->close();
}
?>