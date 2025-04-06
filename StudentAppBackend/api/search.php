<?php
include 'config.php'; // Your database connection file

$query = isset($_GET['query']) ? $_GET['query'] : '';
$category = isset($_GET['category']) ? $_GET['category'] : 'all';

$sql = "SELECT type, title, description FROM search_data WHERE title LIKE ?";

if ($category !== 'all') {
    $sql .= " AND type = ?";
}

$stmt = $conn->prepare($sql);
if ($category === 'all') {
    $stmt->bind_param("s", $searchTerm);
} else {
    $stmt->bind_param("ss", $searchTerm, $category);
}

$searchTerm = "%" . $query . "%";
$stmt->execute();
$result = $stmt->get_result();

$searchResults = [];
while ($row = $result->fetch_assoc()) {
    $searchResults[] = $row;
}

echo json_encode($searchResults);
?>