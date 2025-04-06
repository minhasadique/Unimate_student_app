<?php
$host = "localhost";
$username = "root"; // Default for XAMPP
$password = ""; // Leave empty for XAMPP
$database = "student_app_db"; // The database you will create

// Create a connection
$conn = new mysqli($host, $username, $password, $database);

// Check the connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>