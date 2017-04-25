<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ras测试</title>
<script
	src="http://cdn.static.runoob.com/libs/jquery/1.10.2/jquery.min.js">
</script>
<script type="text/javascript" src="../js/security.js"></script>
<script>
	function individualSubmitForm() {
		 alert($('#exponent').val());
		 alert($('#modulus').val());
		 var key = RSAUtils.getKeyPair($('#exponent').val(), '', $('#modulus').val());
		 alert(key);
		 var password = RSAUtils.encryptedString(key, $('#in_password').val());
			$('#in_password').val(password);
			 alert(password);
		//alert($('#in_password').val());
		//alert($.md5($('#password').val()));
		/*document.forms[1].submit(); */
	}
</script>
</head>
<body>
	密码<input id="in_password" type="text" name="password" />
	<input id="modulus" type="hidden" value="00d492084c3cf4a893d23cb3864602aca8c00f18e948530553d07d49f996172ec809e38faa4a3deb36084f4378759bf7bd3c5486ab1b3a24e700fb282bc0db35e9"/>
	<input id="exponent" type="hidden" value="010001" />
	<button type="button" onclick="individualSubmitForm();">按钮</button>
</body>
</html>