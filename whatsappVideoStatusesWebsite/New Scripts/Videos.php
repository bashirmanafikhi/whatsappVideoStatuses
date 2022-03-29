<?php

//Getting the page number which is to be displayed  
$page = isset($_GET['page']) ? $_GET['page'] : 1;

//Limit is 10 that means we will show 10 items at once
$limit = isset($_GET['limit']) ? $_GET['limit'] : 10;

//Getting Category type
$category = $_GET['category'];

//generate category sql query if exist
$categoryQuery = isset($category) ? "where Category like '$category'" : "";

// Getting sort type
$sortType = "Random";
if (isset($_GET["sortType"])) {
    $sortType = $_GET['sortType'];
}

// search
$keyword = null;
if (isset($_GET["keyword"])) {
    $keyword = $_GET['keyword'];
}
$searchQuery = isset($keyword) ? "where VideoTitle like '%$keyword%'" : $categoryQuery;


// generate sort query
$sortQuery = "ORDER BY RAND()";
switch (strtoupper($sortType)) {
    case 'NEW':
        $sortQuery = "ORDER BY Id DESC";
        break;
    case 'VIEWS':
        $sortQuery = "ORDER BY ViewsCount DESC";
        break;
    case 'RANDOM':
        $sortQuery = "ORDER BY RAND()";
        break;
    case 'LIKES':
        $sortQuery = "ORDER BY LikesCount DESC";
        break;
    case 'DOWNLOADS':
        $sortQuery = "ORDER BY DownloadsCount DESC";
        break;

    default:
        break;
}


//Initially we show the data from 1st row that means the 0th row 
$start = 0;

//Importing the database connection 
require_once('dbConnect.php');


//Counting the total item available in the database 
$total = mysqli_num_rows(mysqli_query($con, "SELECT id from Videos $categoryQuery"));


//We can go atmost to page number total/limit
$page_limit = $total / $limit;

//If the page number is more than the limit we cannot show anything 
if ($page <= $page_limit) {

    //Calculating start for every given page number 
    $start = ($page - 1) * $limit;

    //SQL query to fetch data of a range 
    $sql = "SELECT * from Videos $searchQuery $sortQuery limit $start, $limit";

    //Getting result 
    $result = mysqli_query($con, $sql);

    //Adding results to an array 
    $res = array();


    while ($row = mysqli_fetch_array($result)) {
        $res[] = $row;
    }


    //Displaying the array in json format 
    //echo json_encode($res, JSON_UNESCAPED_UNICODE);

    //print ;
    echo "{videos : " . json_encode($res, JSON_UNESCAPED_UNICODE) . ",videos_count:\"$total\",page_limit:\"$page_limit\",current_page:\"$page\",sortType:\"$sortType\"}";
} else {
    echo "over";
}

echo $sortType;
