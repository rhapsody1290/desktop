<?php
//ɾ��cookie�������ʱ��ȵ�ǰʱ����
setcookie('user','',time()-1);
echo '<script language="javascript">alert("�˳��ɹ�");
	location.replace("/plant/");</script>';
?>