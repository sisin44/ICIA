<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order Document</title>
</head>
<body>
	${ordersInfo }
</body>
<script>
function order() {
	var gInfo = "${goodsInfo}";
	var goods = gInfo.split(":");
	var check = document.getElementsByName("check");
	var count = 0;
	for(i =0; i<check.length; i++){
		if(check[i].checked){
			count++;
		}
	}
	if(count >0){
	var orderInfo = "";
	for(i=0;i<check.length;i++){
		if(check[i].checked){
			orderInfo += (goods[i] + (i==check.length-1?"":":"));
		}
	}
	
	var form = document.createElement("form")
	form.action = "Joomoon?goInfo=" + orderInfo;
	form.method = "post";
	document.body.appendChild(form);
	form.submit();
	
	}
	alert(orderInfo);
}
</script>
</html>