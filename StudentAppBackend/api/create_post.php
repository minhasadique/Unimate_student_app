<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Include database configuration
require '../config/db_config.php';

// Check if the request method is POST
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    http_response_code(405); // Method Not Allowed
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
    exit();
}

// Get POST data
$postAuthor1 = $_POST['postAuthor1'] ?? '';
$postText1 = $_POST['postText1'] ?? '';
$user_id = $_POST['user_id'] ?? ''; // ID of the staff user

// Validate required fields
if (empty($postAuthor1) || empty($postText1) || empty($user_id)) {
    http_response_code(400); // Bad Request
    echo json_encode(["status" => "error", "message" => "All fields are required"]);
    exit();
}

// Handle image upload if provided
$postImage1 = null;
if (isset($_FILES['postImage1']) && $_FILES['postImage1']['size'] > 0) {
    $targetDir = "../uploads/";
    $fileName = basename($_FILES['postImage1']['name']);
    $targetFilePath = $targetDir . $fileName;

    if (move_uploaded_file($_FILES['postImage1']['tmp_name'], $targetFilePath)) {
        $postImage1 = $fileName; // Store only the filename
    } else {
        http_response_code(500);
        echo json_encode(["status" => "error", "message" => "Failed to upload image"]);
        exit();
    }
}

// Insert post into the database
$stmt = $conn->prepare("INSERT INTO posts (postAuthor1, postText1, postImage1, user_id) VALUES (?, ?, ?, ?)");
$stmt->bind_param("sssi", $postAuthor1, $postText1, $postImage1, $user_id);


if ($stmt->execute()) {
    http_response_code(201); // Created
    echo json_encode(["status" => "success", "message" => "Post created successfully"]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Failed to create post"]);
}

// Close connections
$stmt->close();
$conn->close();
?>
<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Include database configuration
require '../config/db_config.php';

// Check if the request method is POST
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    http_response_code(405); // Method Not Allowed
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
    exit();
}

// Get POST data
$postAuthor1 = $_POST['postAuthor1'] ?? '';
$postText1 = $_POST['postText1'] ?? '';
$user_id = $_POST['user_id'] ?? ''; // ID of the staff user

// Validate required fields
if (empty($postAuthor1) || empty($postText1) || empty($user_id)) {
    http_response_code(400); // Bad Request
    echo json_encode(["status" => "error", "message" => "All fields are required"]);
    exit();
}

// Handle image upload if provided
$postImage1 = null;
if (isset($_FILES['postImage1']) && $_FILES['postImage1']['size'] > 0) {
    // Read the image file as binary data
    $postImage1 = file_get_contents($_FILES['postImage1']['tmp_name']);
}

// Insert post into the database
$stmt = $conn->prepare("INSERT INTO posts (postAuthor1, postText1, postImage1, user_id) VALUES (?, ?, ?, ?)");
$stmt->bind_param("sssi", $postAuthor1, $postText1, $postImage1, $user_id);

// Send the binary image data
$stmt->send_long_data(2, $postImage1); // Bind image BLOB data properly

if ($stmt->execute()) {
    http_response_code(201); // Created
    echo json_encode(["status" => "success", "message" => "Post created successfully"]);
} else {
    http_response_code(500);
    echo json_encode(["status" => "error", "message" => "Failed to create post"]);
}

// Close connections
$stmt->close();
$conn->close();
?>
