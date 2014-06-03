
<?php 
/*
CSE 154 AA HW5
Yang Zhang 1030416
This file is the logic of operation from todolist.php
I did both extra feathers
*/
include("common.php");

check_Login();

$filename = "todo_{$_SESSION["cname"]}.txt";
$item_regex = "/^\s*$/";
$index_regex = "/^\d+$/";
$action = $_POST["action"];
# do delete operation
if($action == "delete"){
	$index = $_POST["index"];
	$tasks = file($filename);
	# deal with malicious behaviors
	if(!preg_match($index_regex, $index) || $index>= sizeof($tasks)){
		header("Location: todolist.php?evil=true");
		die();
	}
	$tasks[$index]="";
	$tasks = implode($tasks); 
	file_put_contents($filename, $tasks);
	header("Location: todolist.php");
# do add operation
}else if($action == "add"){
	$record = $_POST["item"];
	if(preg_match($item_regex, $record)){
		header("Location: todolist.php?error=true");
		die();
	}
	$record = $record . "\n";
	file_put_contents($filename,$record,FILE_APPEND);
	header("Location: todolist.php");
# deal with malicious behaviors	
}else{
	header("Location: todolist.php?evil=true");
}
?>