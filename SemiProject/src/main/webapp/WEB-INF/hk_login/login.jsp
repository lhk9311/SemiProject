<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String ctxPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>SISEON | LOGIN</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Google Font -->
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">

<!-- Bootstrap -->
<link rel="stylesheet" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css">

<!-- jQuery -->
<script src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>

<!-- 직접 만든 js -->
<script src="<%=ctxPath%>/js/hk_login/login.js"></script>

<style>
body{font-family:'Poppins',sans-serif!important;background:#fbfaf8!important}

/* 카드 */
.login-wrap{
    max-width:420px!important;
    margin:120px auto!important;
    padding:40px!important;
    background:#fff!important;
    border:1px solid #ddd!important;
    box-shadow:0 8px 20px rgba(0,0,0,.06)!important
}

/* 타이틀 */
.login-title{text-align:center!important;font-size:26px!important;font-weight:500!important;margin-bottom:30px!important;letter-spacing:2px!important}

/* 입력 */
.form-control{height:48px!important;border-radius:0!important}

/* 로그인 */
.btn-login{height:48px!important;background:#4a322b!important;color:#fff!important;border-radius:0!important;font-weight:500!important}
.btn-login:hover{background:#3e2723!important}

/* 옵션 */
.save-id{font-size:.9rem!important}

/* 링크 */
.login-links{text-align:center!important;font-size:.9rem!important;margin-top:20px!important}
.login-links a{color:#555!important;margin:0 8px!important;text-decoration:none!important}
.login-links a:hover{color:#5d4037!important}

/* 회원가입 */
.btn-outline-secondary{
    height:46px!important;
    border-radius:0!important;
    font-size:.95rem!important;
    font-weight:500!important;
    letter-spacing:-.2px!important;
    background:#f1ebe5!important;
    border:1px solid #d2c4ba!important;
    color:#5d4037!important;
    transition:all .2s ease!important
}

.btn-outline-secondary:hover{
    background:#e6dbd2!important;
    border-color:#5d4037!important;
    color:#3e2723!important
}
</style>

</head>

<body>

<!--  고정 헤더 -->
<jsp:include page="../header.jsp" />

<div class="login-wrap">
    <div class="login-title">SISEON</div>

    <!-- 여기가 핵심 !!!  서버로 로그인 요청 보냄 -->
    <form name="loginFrm" method="post" action="<%=ctxPath%>/login.sp">
        <div class="form-group">
            <input type="text" class="form-control" name="userid" id="userid" placeholder="아이디">
        </div>
        <div class="form-group">
            <input type="password" class="form-control" name="passwd" id="pwd" placeholder="비밀번호">
        </div>

        <div class="form-group d-flex align-items-center">
            <input type="checkbox" id="saveid" class="mr-2">
            <label for="saveid" class="mb-0 save-id">아이디 저장</label>
        </div>

        <button type="button" id="btnLogin" class="btn btn-login btn-block">
            LOGIN
        </button>
    </form>

    <div class="login-links">
        <a href="<%= ctxPath %>/idFind.sp">아이디 찾기</a> 
        <a href="<%= ctxPath %>/pwdFind.sp">비밀번호 찾기</a>
    </div>
    
    <hr>

	<!-- 회원가입 버튼 -->
	<button type="button" class="btn btn-outline-secondary btn-block mt-3" onclick="location.href='<%=ctxPath%>/register.sp'">
	    회원가입
	</button>
</div>

<!--  고정 푸터 -->
<jsp:include page="../footer.jsp" />

</body>
</html>