<?php
//�����ݿ��л�ȡ����
function xget($sql){
	//��������
	$con=mysqli_connect("localhost","root","root");
	//ѡ�����ݿ�
	$db=mysqli_select_db($con,"plant");
	//�������ı��룬������
	$charset="set names 'gbk'";
	mysqli_query($con,$charset);
	//�õ������
	$re=mysqli_query($con,$sql);
	//��ȡ�����������
	$num=mysqli_num_rows($re);
	$arr=array();
	for($a=0;$a<$num;$a++){
		//�����еĽ������ӵ�������Ȼ�󷵻�
		$arr[]=mysqli_fetch_assoc($re);
	}
	return $arr;	
}
//����в��롢ɾ�����޸�����
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
