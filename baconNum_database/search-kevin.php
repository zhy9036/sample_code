<!DOCTYPE html>
<?php
	/*
	CSE 154 
	HW 6
	YANG ZHANG
	This is php file for finding all movies with K.B.
	*/
	include("common.php");
	$fname = $_GET["firstname"];
	$lname = $_GET["lastname"];
	$fullname = $fname . " " . $lname;
	# connect to the imdb database
	$db = new PDO("mysql:dbname=imdb", "zhy9036", "SyDiPKFnWk5B3");
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);	
	$ids = findId($db,$lname,$fname);
	$bacon_id = findId($db,"Bacon","Kevin");
	# query the database to see the movie names
	if($id_array = $ids -> fetch()){
		$id_first = $id_array['id'];
		$bacon_id = $bacon_id -> fetch();
		$bacon_id = $bacon_id['id'];
		#query to check if the selected actor is in movies with K.B, 
		#if so return all the movie names and years
		$rows = $db->query("SELECT m.name, m.year FROM movies m 
							JOIN roles r ON m.id = r.movie_id
							JOIN actors a ON a.id = r.actor_id
							JOIN roles r2 ON m.id = r2.movie_id 
							JOIN actors a2 ON a2.id = r2.actor_id 
							WHERE a.id = $id_first  AND a2.id = $bacon_id 
							ORDER BY m.year DESC, m.name ASC");
	}
?>
<html>
	<?=head()?>

	<body>
		<div id="frame">
			<?=topbar()?>
			<div id="main">
				<?php 
				if(!isset($rows)){
					echo "<p>Actor {$fullname} not found.</p>";
				}else if($rows = $rows->fetchall()) { 
					tables($fullname, $rows, "Bacon");
				}else{
					echo "<p>{$fullname} wasn't in any films with Kevin Bacon.</p>";
				}
				forms(); ?>	
			</div>
			<?=bottom()?>
		</div>
	</body>
</html>