<?php
include 'db_config.php'; // Your database connection file

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $message_id = $_POST['message_id'];
    $user_id = $_POST['user_id'];

    // Check if the user is the sender of the message
    $query = "SELECT sender FROM messages WHERE id = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("s", $message_id);
    $stmt->execute();
    $stmt->store_result();
    $stmt->bind_result($sender);
    $stmt->fetch();

    if ($stmt->num_rows > 0 && $sender == $user_id) {
        // User is authorized to delete the message
        $delete_query = "DELETE FROM messages WHERE id = ?";
        $delete_stmt = $conn->prepare($delete_query);
        $delete_stmt->bind_param("s", $message_id);
        if ($delete_stmt->execute()) {
            echo "Message deleted successfully";
        } else {
            echo "Failed to delete message";
        }
    } else {
        echo "Unauthorized";
    }

    $stmt->close();
    $conn->close();
}
?>
