<html>
  <head>
    <!-- ��¼��ע��ҳ�棬������ҳ�汣��һ�µ���ʽ-->
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
		//����tomysql.php�еĺ��������в�ѯƥ��
		$re=xget($sql);
		if(count($re)==1){
			//д��cookie��ȥ
			setcookie('user',$name);
			echo '<script language="javascript">
			alert("��¼�ɹ�");
			//��ת����ҳ�棬·��Ϊ���·��
			location.replace("/plant/");</script>';
		}else{
		echo '<script language="javascript">
		alert("��¼ʧ�ܣ��û����������������");
		</script>';
		}
	}
	?>
     <form action="" method="post">
     �˺�:<input name="name" type="text"/><br/>
     ����:<input name="pwd" type="text"/><br/>
     <input type="submit" value="��¼"/>
     </form>
     </body>
</html>