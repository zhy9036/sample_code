

<?php
/*
CSE 154 AA HW5
Yang Zhang 1030416
This file is the logic for login operation.
I did both extra feathers
*/
session_start();
if(isset($_SESSION["cname"]) || !isset($_GET["normal"])) { #in case that user visit login.php before log in
														   #if so, will redirect to todolist.php and then 
														   #redirect back to start.php
	header("Location: todolist.php");
	die();
}

$exists = false;
$username = $_POST["name"];
$password = $_POST["password"];
$name_regex = "/^[a-z]([a-z]|\d){2,7}$/";
$pw_regex = "/^\d.{4,10}\W$/"; 
$contains_regex = "/.*({$username}).*/";
$infos = file("users.txt",FILE_IGNORE_NEW_LINES);

foreach($infos as $info){
	list($name,$pw)=explode(":",$info);
	if($name==$username){
		$exists=true;
		if($pw==$password){
			remeberMe($username);
			setcookie("date", date("D y M d, g:i:s a"), time()+3600*24*7); #expire after 7 days
			header("Location: todolist.php");
		}else{
			header("Location: start.php?no_match=true"); #password doesn't match its username
			die();
		}
	}	
}

#for first time creating accout, check format for username and password 
if(!$exists){
	if(!preg_match($name_regex,$username)){ #username illegal
		header("Location: start.php?uname_fail=true");
		die();
	}else if(!preg_match($pw_regex,$password)){ #password illegal
		header("Location: start.php?pass_fail=true");
		die();
	}else if(preg_match($contains_regex, $password)){ #password contains username
		header("Location: start.php?contains=true");
		die();
	}
	$record = $username . ":" . $password . "\n";
	file_put_contents("users.txt",$record,FILE_APPEND);
	remeberMe($username);
	setcookie("date", date("D y M d, g:i:s a"), time()+3600*24*7); #expire after 7 days	
	header("Location: todolist.php");
}
#create session for logged in user
function remeberMe($username){
	session_start();
	$_SESSION["cname"] = $username;
}
?>