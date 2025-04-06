<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include("../config/db_config.php");

// Check database connection
if (!$conn) {
    http_response_code(500); // Internal Server Error
    echo json_encode(["status" => "error", "message" => "Database connection failed"]);
    exit();
}

$sql = "SELECT title, description, author, roles, contact FROM startups ORDER BY id DESC";
$result = $conn->query($sql);

if (!$result) {
    http_response_code(500); // Internal Server Error
    echo json_encode(["status" => "error", "message" => "Database query failed"]);
    exit();
}

$startups = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $startups[] = $row;
    }
}

// Ensure valid JSON response
http_response_code(200); // Success
//echo json_encode(["status" => "success", "data" => $startups], JSON_PRETTY_PRINT);
echo json_encode( $startups, JSON_PRETTY_PRINT);

?>