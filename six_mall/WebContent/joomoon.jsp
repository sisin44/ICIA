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
	
	var ordersType = "${ordersType}";
	var gInfo = "${goodsInfo}";
	var message = "${message}";
	var form = document.createElement("form")
	if(ordersType == "Order"){
		form.action = "Order?gInfo="+ gInfo;
	}else if(ordersType == "BasketOrder"){
		form.action = "BasketOrder?gInfo=" + gInfo;
	}

	form.method = "post";
	document.body.appendChild(form);
	form.submit();
	if(message != ""){
		alert(message);
	}
}

function orderCancel() {
	var gInfo = "${goodsInfo}";
	
	var form = document.createElement("form");

	form.action = "orderCancel?gInfo="+ gInfo;
	form.method="post";
	document.body.appendChild(form);
	form.submit();
}


</script>
</html>