<?php 
	$errors = file("errors.txt");
	$error_code = (isset($_GET["error"])) ? $_GET["error"] : -1;
	if($error_code>=0){?>
		<div id="error">	
			<span><?=$errors[$error_code]?></span>
		</div>
 <?php } ?>
?>