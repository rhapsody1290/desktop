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
			echo '<script language="javascript">alert("ע��ɹ�");
			location.replace("/plant/");</script>';
		}
		else{
		echo '<script language="javascript">
		alert("���û��ѱ�ע��");
		</script>';
		}
	}
	else{
		echo '<script language="javascript">
		alert("ע��ʧ�ܣ��û�������������д����ȷ");
		</script>';
	}
   }
   ?>
   <form action="" method="post">
   ��&nbsp;&nbsp;&nbsp;��:<input name="name" type="text"/><br/>
   ��&nbsp;&nbsp;&nbsp;��:<input name="pwd" type="text"/><br/>
   <input type="submit" value="ע��"/>
   </form>
  </body>
</html>