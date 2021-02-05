<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <meta name="description" content="Login Page">
    <meta name="author" content="Sookyeong">
    <link rel="icon" type="image/png" href="icia-logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body onLoad="init()">
    <!-- Header -->
    <header id="header">
        <h1><a href="https://www.icia.co.kr/"><img src="image/icia-logo.png" alt="어서와~~~~~"></a></h1>
    </header>
    <!-- Secion -->
    <section id="section">
        <div id="memberInfo" class="userInfo">
			<marquee>${memberInfo }</marquee>              
        </div>
        <div id="sendZone"></div>   
    </section>
    <br/>
    <!-- Footer -->
    <footer id="footer">
        <span class="footer__icon"><a href="https://www.icia.co.kr/"><img src="image/icia-logo.png" alt="" id="footer__icon"></a></span>
        <span class="footer__rights">Copyright <b>ICIA.</b> All Rights Reserved.</span>
    </footer>
    <input type="hidden" name="member" value="${accessInfo }" />
</body>
<script>
function init(){
	var accessInfo = "${accessInfo}";
	var message = "${param.message}";
	var sendZone = document.getElementById("sendZone");
	
	if (accessInfo == "") {
		// 로그인 X -->  
		document.getElementById("memberInfo").style.display = "none";
		sendZone.innerHTML = "<input type='button' value='LOGIN' onClick='sendForm(this)'/>";
	} else {
		// 로그인 O
		document.getElementById("memberInfo").style.display = "block";
		sendZone.innerHTML = "<input type='button' value='LOG OUT' onClick='sendLogOut()'/>";
	}
	
	if (message != ""){ alert(message);	}
}

function sendForm(obj) {
	// Dynamic Element Creation
	var form = document.createElement("form");
	form.action = "LogInForm";
	form.method = "get";

	// Current Page정보
	var page = document.createElement("input");
	page.type = "hidden";
	page.name = "page";
	page.value = "Main";
	form.appendChild(page);

	document.body.appendChild(form);
	form.submit();
}

function sendLogOut(){
	// Dynamic Element Creation
	var form = document.createElement("form");
	form.action = "LogOut";
	form.method = "post";
		
	// LogOut을 할 멤버 정보 가져오기
	form.appendChild(document.getElementsByName("member")[0]);
	
	document.body.appendChild(form);
	form.submit();
}
</script>
</html>