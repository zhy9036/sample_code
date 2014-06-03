<?php 
/*
CSE 154 AA HW5
Yang Zhang 1030416
This page kills session for logging out
I did both extra feathers
*/
	include("common.php");

	check_Login();
	#destroy current user session
	session_destroy();
	header("Location: start.php");
?>