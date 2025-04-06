<?php
require '../config/db.php'; // Include your database connection

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $user_id = $_POST['user_id'];
    $text = $_POST['text'];
    $media_type = $_POST['media_type']; // 'image', 'video', or ''
    $media_url = "";

    if (isset($_FILES['media'])) {
        $target_dir = "../uploads/";
        $file_name = time() . "_" . basename($_FILES['media']['name']);
        $target_file = $target_dir . $file_name;
        $media_url = "https://yourdomain.com/uploads/" . $file_name; // Change to your actual domain

        if (move_uploaded_file($_FILES['media']['tmp_name'], $target_file)) {
            $response['message'] = "File uploaded successfully!";
        } else {
            $response['error'] = true;
            $response['message'] = "File upload failed!";
            echo json_encode($response);
            exit;
        }
    }

    // Insert into database
    $stmt = $conn->prepare("INSERT INTO posts (user_id, text, media_type, media_url) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("ssss", $user_id, $text, $media_type, $media_url);

    if ($stmt->execute()) {
        $response['error'] = false;
        $response['message'] = "Post uploaded successfully!";
    } else {
        $response['error'] = true;
        $response['message'] = "Database error!";
    }

    $stmt->close();
} else {
    $response['error'] = true;
    $response['message'] = "Invalid request!";
}

echo json_encode($response);
?>
