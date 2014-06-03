<!DOCTYPE html>
<!-- 
CSE 154 AA HW5
Yang Zhang 1030416
This file it to display users' memo
I did both extra feathers
-->


	<?php 
	include("common.php");
	
	check_Login();
	
	$index = 0;

	$name = $_SESSION["cname"];
	$filename = "todo_{$name}.txt";
	#create a memo file for user if there isn't one
	if(!file_exists($filename)){
		fopen($filename, 'x+');
	}
	$tasks = file($filename);
	
	?>
<html>
	<?=head()?>
	<body>
		<?=logo()?>
		
		<div id="main">
			<?php if(isset($_GET["error"])){ ?>
				<div id="error">
					<span>To-do item cannot be blank.</span>
				</div>
			<?php }else if(isset($_GET["evil"])){ ?>
				<div id="error">
					<span>Malicious behavior detected! Don't temper with my code!</span>
				</div>
			<?php } ?>

			<h2><?=$name?>'s To-Do List</h2>

			<ul id="todolist">
				<?php foreach($tasks as $task){ ?>
					<li>
						<form action="submit.php" method="post">
							<input type="hidden" name="action" value="delete" />
							<input type="hidden" name="index" value="<?=$index?>" />
							<input type="submit" value="Delete" />
						</form>
						<?php 
						echo htmlspecialchars($task); #ecoding memo
						$index++;
						 ?>
					</li>
				<?php } ?>
				<li>
					<form action="submit.php" method="post">
						<input type="hidden" name="action" value="add" />
						<input name="item" type="text" size="25" autofocus="autofocus" />
						<input type="submit" value="Add" />
					</form>
				</li>
			</ul>

			<div>
				<a href="logout.php"><strong>Log Out</strong></a>				
				<em>(logged in since <?=$_COOKIE["date"]?>)</em>
			</div>

		</div>

		<?=bottom()?>
	</body>
</html>
