<?php
//从数据库中获取数据
function xget($sql){
	//连接主机
	$con=mysqli_connect("localhost","root","root");
	//选择数据库
	$db=mysqli_select_db($con,"plant");
	//设置中文编码，防乱码
	$charset="set names 'gbk'";
	mysqli_query($con,$charset);
	//得到结果集
	$re=mysqli_query($con,$sql);
	//获取结果集的行数
	$num=mysqli_num_rows($re);
	$arr=array();
	for($a=0;$a<$num;$a++){
		//把所有的结果都添加到该数组然后返回
		$arr[]=mysqli_fetch_assoc($re);
	}
	return $arr;	
}
//向表中插入、删除、修改数据
function xq($sql){
	$con=mysqli_connect("localhost","root","root");
	$db=mysqli_select_db($con,"plant");
	//$charset="set names 'utf8_general_ci'";
	$charset="set names 'gbk'";
	mysqli_query($con,$charset);
	$re=mysqli_query($con,$sql);
	return $re;		
}
?>
