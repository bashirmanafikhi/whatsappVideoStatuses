<?php

//Importing the database connection 
require_once('dbConnect.php');


//SQL query to fetch videos count
$sql = "SELECT COUNT(*) from Videos;";

//Getting result 
$query = mysqli_query($con, $sql);
$row = mysqli_fetch_row($query);

$total = $row[0];

//print ;
echo $total;
