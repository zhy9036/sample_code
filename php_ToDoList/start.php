
<!DOCTYPE html>
<!-- 
CSE 154 AA HW5
Yang Zhang 1030416
This is initial page for user to login.
I did both extra feathers
-->
<html>
	<?php include("common.php"); 
		  session_start();  
		  if(isset($_SESSION["cname"])){ #in case that user back to this page without logout
		  		header("Location:todolist.php");
		  		die();
		  }
		  head(); ?>

	<body>
		<?=logo()?>
		
		<div id="main">
			<?php 
				#error block
			if(isset($_GET["uname_fail"])){ ?> 
				<div id="error">
					<span>Invalid user name. Must begin with 
					a letter and consist of 3-8 letters/numbers.</span>
				</div>
			<?php }else if(isset($_GET["pass_fail"])){ ?>
				<div id="error">	
					<span>Invalid password. Must consist of 6-12 characters,  
					begin with a number, and end with a character that is not
					a letter or number.</span>
				</div>
			<?php }else if(isset($_GET["contains"])){ ?>
				<div id="error">	
					Creation fail: <span>Password should not contain username</span>
				</div>
			<?php }else if(isset($_GET["no_match"])){ ?>
				<div id="error">	
					<span>In correct password.</span>
				</div>
		<?php }?>
			<p>
				The best way to manage your tasks. <br />
				Never forget the cow (or anything else) again!
			</p>

			<p>
				Log in now to manage your to-do list. <br />
				If you do not have an account, one will be created for you.
			</p>

			<form id="loginform" action="login.php?normal=true" method="post">
				<div><input name="name" type="text" size="8" autofocus="autofocus" /> <strong>User Name</strong></div>
				<div><input name="password" type="password" size="8" /> <strong>Password</strong> </div>
				<div><input type="submit" value="Log in" /></div>
			</form>
			<?php if(isset($_COOKIE["date"])){?>
			<p>
				<em>(last login from this computer was <?=$_COOKIE["date"]?>)</em>
			</p>
			<?php }?>
		</div>

		<?=bottom()?>
	</body>
</html>
