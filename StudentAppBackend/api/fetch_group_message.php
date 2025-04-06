<?php
include 'config.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    $group_id = $_GET['group_id'];

    $stmt = $conn->prepare("SELECT * FROM group_messages WHERE group_id = ? ORDER BY timestamp ASC");
    $stmt->bind_param("i", $group_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $messages = [];
    while ($row = $result->fetch_assoc()) {
        $messages[] = $row;
    }

    echo json_encode($messages);
    $stmt->close();
}
?><div e3dddfw="">                                      </div>