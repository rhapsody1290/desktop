<html>
  <head>
    <title>Plant</title>
    <link type="text/css" rel="stylesheet" href="index.css" />  
  </head>
  <body>
  <h1>Plant</h1>
  <?php
   include 'tomysql.php';
   if($_POST){
	if(isset($_POST['name'])& isset($_POST['pwd'])){
		$name=$_POST['name'];
		$pwd=$_POST['pwd'];
		$sql="insert into user(name,pwd)values('$name','$pwd')";
		$re=xq($sql);
		if($re){
			setcookie('user',$name);
			echo '<script language="javascript">alert("注册成功");
			location.replace("/plant/");</script>';
		}
		else{
		echo '<script language="javascript">
		alert("该用户已被注册");
		</script>';
		}
	}
	else{
		echo '<script language="javascript">
		alert("注册失败，用户名或者密码填写不正确");
		</script>';
	}
   }
   ?>
   <form action="" method="post">
   帐&nbsp;&nbsp;&nbsp;号:<input name="name" type="text"/><br/>
   密&nbsp;&nbsp;&nbsp;码:<input name="pwd" type="text"/><br/>
   <input type="submit" value="注册"/>
   </form>
  </body>
</html>