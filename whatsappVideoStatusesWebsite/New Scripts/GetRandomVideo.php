<?php

//Importing the database connection 
require_once('dbConnect.php');


//SQL query to fetch a random video
$sql = "SELECT * from Videos $categoryQuery ORDER BY RAND() LIMIT 1;";


//Getting result 
$query = mysqli_query($con, $sql);

$row = mysqli_fetch_array($query);


//print ;
echo json_encode($row, JSON_UNESCAPED_UNICODE);
