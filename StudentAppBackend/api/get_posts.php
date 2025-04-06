<?php
require '../config/db_config.php'; // Adjust the path if needed

header("Content-Type: application/json");


$sql = "SELECT id, postAuthor1, postText1, postImage1, created_at FROM posts ORDER BY created_at DESC";
$result = $conn->query($sql);

$posts = [];
while ($row = $result->fetch_assoc()) {
    if (!empty($row['postImage1'])) {
        $row['postImage1'] = "data:image/jpeg;base64," . base64_encode($row['postImage1']);
    }
    $posts[] = $row;
}

// <!-- "status" => "success", "posts" -->
echo json_encode( $posts);

$conn->close();   
?>


