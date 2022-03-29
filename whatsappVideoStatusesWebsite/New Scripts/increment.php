<?php 

     include_once 'init.php';

     
     $id =  (int)$_POST['key'];
     $property =   $_POST['property'];




     if ($property == 'views' && !empty($id)){
            $sql = "UPDATE Videos SET ViewsCount=ViewsCount+1 WHERE Id=$id";
            mysqli_query( $conn, $sql);
            echo '{تمت الإضافة بنجاح}';
     } elseif ($property == 'likes' && !empty($id)){
            $sql = "UPDATE Videos SET LikesCount=LikesCount+1 WHERE Id=$id";
            mysqli_query( $conn, $sql);
            echo '{تمت الإضافة بنجاح}';
     } elseif ($property == 'downloads' && !empty($id)){
            $sql = "UPDATE Videos SET DownloadsCount=DownloadsCount+1 WHERE Id=$id";
            mysqli_query( $conn, $sql);
            echo '{تمت الإضافة بنجاح}';
     } else {
        echo 'there is no property inserted';
     }

?>