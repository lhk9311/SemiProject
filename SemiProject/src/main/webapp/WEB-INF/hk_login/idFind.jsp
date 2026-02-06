<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>SISEON | ID FIND</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Google Font -->
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">

<!-- Bootstrap -->
<link rel="stylesheet" href="<%=ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css">

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

/* input */
.form-control{height:48px!important;border-radius:0!important}

/* 메인 버튼 */
.btn-login{
    height:48px!important;
    background:#4a322b!important;
    color:#fff!important;
    border-radius:0!important;
    font-weight:500!important
}
.btn-login:hover{background:#3e2723!important}

/* 결과 */
.result-box{margin-top:30px;text-align:center}

/* 링크 */
.link-area{text-align:center;font-size:.9rem;margin-top:25px!important}
.link-area a{color:#555!important;margin:0 8px!important;text-decoration:none!important}
.link-area a:hover{color:#5d4037!important}

</style>

<script>
function validateForm() {

    const name  = document.querySelector('input[name="name"]').value.trim();
    const email = document.querySelector('input[name="email"]').value.trim();

    if(name === "") {
        alert("성명을 입력하세요.");
        return false; // 서버 요청 차단
    }

    if(email === "") {
        alert("이메일을 입력하세요.");
        return false; // 서버 요청 차단
    }

    const regExp =
    /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

    if(!regExp.test(email)) {
        alert("이메일 형식이 올바르지 않습니다.");
        return false; // 서버 요청 차단
    }

    return true; // 검증 통과 시 Controller 호출
}
</script>
</head>

<body>

<!-- 헤더 -->
<jsp:include page="../header.jsp" />

<div class="login-wrap">

    <div class="login-title">ID FIND</div>

    <!-- 아이디 찾기 폼 -->
    <form method="post" action="<%= ctxPath %>/idFind.sp" onsubmit="return validateForm();">

        <div class="form-group">
            <input type="text"
                   name="name"
                   class="form-control"
                   placeholder="성명"
                   value="${name}">
        </div>

        <div class="form-group">
            <input type="text"
                   name="email"
                   class="form-control"
                   placeholder="이메일"
                   value="${email}">
        </div>

        <button type="submit" class="btn btn-login btn-block">
            아이디 찾기
        </button>
    </form>

    <!-- 결과 출력 -->
    <c:if test="${not empty name}">
        <div class="result-box">
            <hr>
            <c:choose>
                <c:when test="${not empty userid}">
                    <p>회원님의 아이디는</p>
                    <p style="color:#5D4037; font-size:18px; font-weight:600;">
                        ${userid}
                    </p>
                    <p>입니다.</p>
                </c:when>
                <c:otherwise>
                    <p style="color:red;">
                        일치하는 회원 정보가 없습니다.
                    </p>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
    
    <!-- 이동 링크 -->
    <div class="link-area">
        <a href="<%= ctxPath %>/login.sp">로그인</a> |
        <a href="<%= ctxPath %>/pwdFind.sp">비밀번호 찾기</a>
    </div>

</div>

<!-- 푸터 -->
<jsp:include page="../footer.jsp" />

</body>
</html>
