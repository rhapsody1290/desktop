<?php
//删除cookie，令过期时间比当前时间早
setcookie('user','',time()-1);
echo '<script language="javascript">alert("退出成功");
	location.replace("/plant/");</script>';
?>