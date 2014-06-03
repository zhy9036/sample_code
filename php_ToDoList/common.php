
<?php
/*
CSE 154 AA HW5
Yang Zhang 1030416
This file contains common functions.
I did both extra feathers

*/
#function for producing head of html
function head(){ ?>
	<head>
		<meta charset="utf-8" />
		<title>Remember the Cow</title>
		<link href="https://webster.cs.washington.edu/css/cow-provided.css" type="text/css" rel="stylesheet" />
		<link href="cow.css" type="text/css" rel="stylesheet" />
		<link href="https://webster.cs.washington.edu/images/todolist/favicon.ico" type="image/ico" rel="shortcut icon" />
		<script src="https://webster.cs.washington.edu/js/todolist/provided.js" type="text/javascript"></script>
	</head>
<?php }   

#function for producing logo and top bar
function logo() { ?>
	<div class="headfoot">
		<h1>
			<img src="https://webster.cs.washington.edu/images/todolist/logo.gif" alt="logo" />
			Remember<br />the Cow
		</h1>
	</div>
<?php } 

#function for producing bottom stuff
function bottom() { ?>
	<div class="headfoot">
		<p>
			"Remember The Cow is nice, but it's a total copy of another site." - PCWorld<br />
			All pages and content &copy; Copyright CowPie Inc.
		</p>

		<div id="w3c">
			<a href="https://webster.cs.washington.edu/validate-html.php">
				<img src="https://webster.cs.washington.edu/images/w3c-html.png" alt="Valid HTML" /></a>
			<a href="https://webster.cs.washington.edu/validate-css.php">
				<img src="https://webster.cs.washington.edu/images/w3c-css.png" alt="Valid CSS" /></a>
		</div>
	</div>
<?php } 

#function for checking login state
function check_Login(){
	session_start(); 
	if(!isset($_SESSION["cname"])){
		header("Location: start.php");
		die();
	}
}

?>