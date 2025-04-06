<?php
require_once "../config/db_config.php";

$sql = "SELECT id, name FROM users";
$result = mysqli_query($conn, $sql);

$users = [];
while ($row = mysqli_fetch_assoc($result)) {
    $users[] = $row;
}

echo json_encode(["success" => true, "users" => $users]);
?>