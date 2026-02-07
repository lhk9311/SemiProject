<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String ctxPath = request.getContextPath();
%>

<jsp:include page="../header.jsp" />

<link rel="stylesheet" as="style" crossorigin
      href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/static/pretendard.css" />
            
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="<%=ctxPath%>/js/jquery-3.7.1.min.js"></script>

<style>
    body { background-color:#fff; font-family:'Pretendard', sans-serif; color:#333; }
    .register-container { max-width:550px; margin:60px auto; padding:20px; }
    .title { text-align:center; font-size:26px; font-weight:700; margin-bottom:50px; letter-spacing:5px; }
    .section-title { font-size:14px; font-weight:bold; margin:40px 0 20px; border-bottom:1px solid #000; }
    .form-label { font-size:13px; font-weight:600; margin-bottom:8px; display:block; }
    .guide-text { font-size:12px; color:#888; margin-bottom:10px; }
    .form-control { border-radius:0; border:1px solid #ddd; padding:12px; }
    .btn-black { background:#000; color:#fff; border-radius:0; padding:16px; width:100%; font-weight:bold; }
    .btn-post { background:#f8f8f8; border:1px solid #ddd; border-radius:0; font-size:13px; }
    .section-title::after, 
    .section-title::before {
        content: none !important; /* 내용 삭제 */
        display: none !important; /* 공간 삭제 */
    }
    /* 이용약관 */
    .terms-box { border:1px solid #ddd; height:240px; width:100%; margin-bottom:15px; }
	.terms-iframe { width:100%; height:100%;  border:none; }
	.terms-agree { font-size:13px; margin-bottom:30px; }
    
</style>

<script>
/* ================================
전역 변수
================================ */

let isIdChecked = false; // 아이디 중복검사 여부


/* ================================
   다음 우편번호
   ================================ */
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.registerFrm.postcode.value = data.zonecode;
            document.registerFrm.address.value  = data.roadAddress;
            document.registerFrm.detailaddress.focus();
        }
    }).open();
}

/* ================================
   회원가입 유효성 검사
   ================================ */
function goRegister() {

    const frm = document.registerFrm;
    const agree = document.getElementById("agreeTerms");
    
    if (frm.userid.value.trim() === "") {
        alert("아이디를 입력하세요.");
        frm.userid.focus();
        return;
    }

    if(!isIdChecked){
        alert("아이디 중복확인을 해주세요.");
        return;
    }
    
    if (frm.passwd.value.trim() === "") {
        alert("비밀번호를 입력하세요.");
        frm.passwd.focus();
        return;
    }

    if (frm.passwd.value !== frm.passwdConfirm.value) {
        alert("비밀번호가 일치하지 않습니다.");
        frm.passwdConfirm.focus();
        return;
    }

    if (frm.name.value.trim() === "") {
        alert("이름을 입력하세요.");
        frm.name.focus();
        return;
    }

    if (frm.email.value.trim() === "") {
        alert("이메일을 입력하세요.");
        frm.email.focus();
        return;
    }

    if (frm.postcode.value.trim() === "") {
        alert("주소를 입력하세요.");
        return;
    }
    
    if(!agree.checked){
        alert("이용약관에 동의하셔야 가입이 가능합니다.");
        return;
    }

    frm.submit();
}


/* ================================
아이디 중복확인 (AJAX)
================================ */
function checkId() {

 const userid = $("#userid").val().trim();

 if(userid === ""){
     alert("아이디를 입력하세요.");
     $("#userid").focus();
     return;
 }

 // 아이디 규칙: 영문+숫자 4~12자
 const regExp = /^[a-zA-Z0-9]{4,12}$/;
 if(!regExp.test(userid)){
     alert("아이디는 영문자와 숫자 4~12자만 가능합니다.");
     return;
 }

 $.ajax({
     url: "<%=ctxPath%>/idCheck.sp",
     type: "get",
     data: { userid: userid },
     dataType: "json",
     success: function(json){
         if(json.isExists){
             alert("이미 사용 중인 아이디입니다.");
             isIdChecked = false;
         }
         else{
             alert("사용 가능한 아이디입니다.");
             isIdChecked = true;
         }
     },
     error: function(){
         alert("아이디 중복검사 실패");
     }
 });
}

</script>

<div class="register-container">
    <div class="title">JOIN US</div>

    <form name="registerFrm" method="post" action="<%=ctxPath%>/register.sp">

        <div class="section-title">BASIC INFO</div>

        <label class="form-label">아이디 *</label>
        <div class="input-group mb-3">
            <input type="text" id="userid" name="userid" class="form-control" placeholder="아이디">
            <div class="input-group-append">
                <button type="button" class="btn btn-post" onclick="checkId()">중복확인</button>
            </div>
        </div>

        <label class="form-label">비밀번호 *</label>
        <p class="guide-text">(8~16자)</p>
        <input type="password" name="passwd" class="form-control">

        <label class="form-label">비밀번호 확인 *</label>
        <input type="password" name="passwdConfirm" class="form-control">

        <label class="form-label">이름 *</label>
        <input type="text" name="name" class="form-control">

        <label class="form-label">이메일 *</label>
        <input type="email" name="email" class="form-control">

        <label class="form-label">연락처</label>
        <input type="text" name="mobile" class="form-control">

        <div class="section-title">ADDRESS</div>

        <div class="input-group mb-2">
            <input type="text" name="postcode" class="form-control" readonly>
            <div class="input-group-append">
                <button type="button" class="btn btn-post" onclick="execDaumPostcode()">주소검색</button>
            </div>
        </div>

        <input type="text" name="address" class="form-control mb-2" readonly>
        <input type="text" name="detailaddress" class="form-control mb-2">
        <input type="text" name="extraaddress" class="form-control">

		<!-- 이용약관 -->
		<div class="section-title">TERMS & CONDITIONS</div>

			<div class="terms-box">
			    <iframe src="<%=ctxPath%>/terms_iframe.sp" class="terms-iframe"></iframe>
			</div>
			
			<div class="terms-agree">
			    <label>
			        <input type="checkbox" id="agreeTerms">
			        <span>이용약관 및 개인정보처리방침에 동의합니다. (필수)</span>
			    </label>
			</div>	

        <div class="section-title">ADDITIONAL INFO</div>

        <label class="form-label">성별</label>
        <div class="mb-3">
            <input type="radio" name="gender" value="1" checked> 남자
            <input type="radio" name="gender" value="2"> 여자
        </div>

        <label class="form-label">생년월일</label>
        <input type="date" name="birthday" class="form-control">

        <button type="button" class="btn-black mt-5" onclick="goRegister()">CREATE ACCOUNT</button>
    </form>
</div>

<jsp:include page="../footer.jsp" />
