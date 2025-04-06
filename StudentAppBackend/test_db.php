<?php
error_reporting(E_ALL); // Enable error reporting
ini_set('display_errors', 1); // Show errors on the browser

include('config/db_config.php');

// Test the connection
if ($conn) {
    echo "Database connected successfully!";
} else {
    echo "Failed to connect to the database.";
}
?>