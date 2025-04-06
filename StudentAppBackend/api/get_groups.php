<?php
require_once "../config/db_config.php";

$sql = "SELECT * FROM groups";
$result = mysqli_query($conn, $sql);

$groups = [];
while ($row = mysqli_fetch_assoc($result)) {
    $groups[] = $row;
}

echo json_encode(["success" => true, "groups" => $groups]);
?>