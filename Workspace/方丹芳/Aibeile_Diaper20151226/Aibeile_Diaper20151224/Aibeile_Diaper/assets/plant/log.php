<html>
  <head>
    <!-- 登录、注册页面，均和主页面保持一致的样式-->
    <title>Plant</title>
    <link type="text/css" rel="stylesheet" href="index.css" />  
  </head>
  <body>
  	<h1>Plant</h1>	
	<?php
	include 'tomysql.php';
	if($_POST){
		$name=$_POST['name'];
		$pwd=$_POST['pwd'];
		$sql="select * from user where 
			name='$name' and pwd='$pwd'";
		//调用tomysql.php中的函数，进行查询匹配
		$re=xget($sql);
		if(count($re)==1){
			//写到cookie中去
			setcookie('user',$name);
			echo '<script language="javascript">
			alert("登录成功");
			//跳转到主页面，路径为相对路径
			location.replace("/plant/");</script>';
		}else{
		echo '<script language="javascript">
		alert("登录失败，用户名或密码输入错误");
		</script>';
		}
	}
	?>
     <form action="" method="post">
     账号:<input name="name" type="text"/><br/>
     密码:<input name="pwd" type="text"/><br/>
     <input type="submit" value="登录"/>
     </form>
     </body>
</html>