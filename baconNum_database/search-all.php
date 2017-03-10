<!DOCTYPE html>
<?php
	/*
	CSE 154 
	HW 6
	YANG ZHANG
	This is php file for finding all movies
	*/
	include("common.php");
	$fname = $_GET["firstname"];
	$lname = $_GET["lastname"];
	$fullname = $fname . " " . $lname;
	# connect to the imdb database
	$db = new PDO("mysql:dbname=imdb", "zhy9036", "SyDiPKFnWk5B3");
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$ids = findID($db, $lname, $fname);
	# query the database to see the movie names and years of the passed in actor
	if($id_array = $ids -> fetch()){
		$id_first = $id_array['id'];
		$rows = $db->query("SELECT m.name, m.year FROM movies m 
							JOIN roles r ON m.id = r.movie_id
							JOIN actors a ON a.id = r.actor_id 
							WHERE a.id = $id_first
							ORDER BY m.year DESC, m.name ASC");
	}
?>
<html>
	<?=head()?>

	<body>
		<div id="frame">
			<?=topbar()?>
			<div id="main">
				<?php if(!isset($rows)){ ?>
					<p>Actor <?=$fullname?> not found.</p>
				<?php }else{	
					tables($fullname,$rows,"all");
				} 
				forms(); ?>	
			</div>
			<?=bottom()?>
		</div>
	</body>
</html>