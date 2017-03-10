<?php 
/*
	CSE 154 
	HW 6
	YANG ZHANG
	This common function file
	*/

#function for producing top bar
function topbar(){ ?>
	<div id="banner">
		<a href="index.php"><img src="https://webster.cs.washington.edu/images/kevinbacon/mymdb.png" alt="banner logo" /></a>
		My Movie Database
	</div>
<?php }

#function for producing html head
function head(){ ?>
	<head>
		<title>My Movie Database (MyMDb)</title>
		<meta charset="utf-8" />
		
		<!-- Links to provided files.  Do not edit or remove these links -->
		<link href="https://webster.cs.washington.edu/images/kevinbacon/favicon.png" type="image/png" rel="shortcut icon" />
		<script src="https://webster.cs.washington.edu/js/kevinbacon/provided.js" type="text/javascript"></script>

		<!-- Link to your CSS file that you should edit -->
		<link href="bacon.css" type="text/css" rel="stylesheet" />
	</head>
<?php }

#function for producing bottom
function bottom(){ ?>
	<div id="w3c">
		<a href="https://webster.cs.washington.edu/validate-html.php"><img src="https://webster.cs.washington.edu/images/w3c-html.png" alt="Valid HTML5" /></a>
		<a href="https://webster.cs.washington.edu/validate-css.php"><img src="https://webster.cs.washington.edu/images/w3c-css.png" alt="Valid CSS" /></a>
	</div>

<?php }

#function for producing html forms
function forms(){ ?>
	<form action="search-all.php" method="get">
		<fieldset>
			<legend>All movies</legend>
			<div>
				<input name="firstname" type="text" size="12" placeholder="first name" autofocus="autofocus" /> 
				<input name="lastname" type="text" size="12" placeholder="last name" /> 
				<input type="submit" value="go" />
			</div>
		</fieldset>
	</form>

	<!-- form to search for movies where a given actor was with Kevin Bacon -->
	<form action="search-kevin.php" method="get">
		<fieldset>
			<legend>Movies with Kevin Bacon</legend>
			<div>
				<input name="firstname" type="text" size="12" placeholder="first name" /> 
				<input name="lastname" type="text" size="12" placeholder="last name" /> 
				<input type="submit" value="go" />
			</div>
		</fieldset>
	</form>

<?php }

#function for producing result tables
function tables($fullname, $rows, $type){ 
	$index = 1;
	$caption = ($type == "all") ? "All Films" : "Films with {$fullname} and Kevin Bacon"?>
	<h1>Results for <?=$fullname?></h1>
		<table>
			<caption><?=$caption?></caption>
			<tr><th>#</th><th>Title</th><th>Year</th></tr>
			<?php foreach ($rows as $row) {
				$type = ($index%2 == 1) ? "class=\"dark\"" : "";
				?>
				<tr <?=$type?> > 
					<td><?=$index?></td>
					<td><?=$row["name"]?></td>
					<td><?=$row["year"]?></td>
				</tr>
				<?php $index++;
			}
			?>
		</table>
<?php } 

#function that finds actor's id and by querring the database and return the object
function findId($db, $lname, $fname){
	$lname = $db->quote($lname);
	$fname = $db->quote($fname . "%");
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	#query the database to find the id of the passed in actor
	$ids = $db->query("SELECT id from actors where first_name like $fname AND last_name = $lname 
		ORDER BY film_count DESC, id ASC");
	return $ids;
}?>




