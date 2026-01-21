<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% String ctxPath = request.getContextPath(); %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>관리자 | 회원목록</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- Font Awesome (아이콘용) -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">

<style>
body{font-family:'Pretendard',Arial,sans-serif;background:#f7f6f3;color:#333}
.container{width:1100px;margin:60px auto;background:#fff;padding:40px;border-radius:4px;box-shadow:0 4px 12px rgba(0,0,0,.06)}
h3{font-size:18px;font-weight:700;letter-spacing:-.3px;color:#2f2b2a;margin-bottom:0}
.container>div a{background:#3e3a39!important;transition:.2s}
.container>div a:hover{background:#2f2b2a!important}

/* ===== 회원 조회 ===== */
.search-area{background:#fff;padding:30px;border-radius:4px;margin-bottom:40px;box-shadow:0 4px 12px rgba(0,0,0,.05)}
.search-area h3{margin-bottom:20px;font-size:16px;font-weight:600}
.search-area form{display:flex;gap:10px;align-items:center}
.search-area select,.search-area input{padding:10px 12px;border:1px solid #ccc;border-radius:3px;font-size:14px}
.search-area input{flex:1}
.search-area form{max-width:700px}
.search-area button{padding:10px 18px;border:none;background:#6d4c41;color:#fff;font-weight:600;cursor:pointer;border-radius:3px}
.search-area button:hover{background:#5d4037}

/* ===== 전체목록 버튼 ===== */
a.reset-btn,a.reset-btn:link,a.reset-btn:visited{display:inline-block;padding:10px 18px;background:#9a9693!important;color:#fff!important;font-size:14px;font-weight:600;border-radius:3px;text-decoration:none;transition:.2s}
a.reset-btn:hover,a.reset-btn:active{background:#a89288;color:#fff!important}

/* ===== 테이블 ===== */
table{width:100%;border-collapse:collapse;margin-top:30px;font-size:14px}
thead th{background:#f2f1ee;font-weight:600;color:#555;padding:14px 10px;border-bottom:2px solid #ddd}
tbody td{padding:14px 10px;border-bottom:1px solid #eee;color:#444}
tbody tr:hover{background:#faf9f7}
tbody td:last-child{font-weight:600}
tbody tr td[colspan]{padding:30px 0;color:#777;font-size:14px}

/* 테이블 헤더 아이콘 */
table th i{margin-right:6px;color:#8d6e63;font-size:13px}

/* ===== 페이지 바 ===== */
.pagination .page-item .page-link{color:#3b2f2a;border:1px solid #e3e0dc;background:#fff;margin:0 3px;padding:6px 12px;font-size:13px;border-radius:20px;transition:.2s}
.pagination .page-link:hover{background:#f5f3ef!important;border-color:#3b2f2a!important;color:#3b2f2a!important}
.pagination .page-item.active .page-link{background:#3b2f2a!important;border-color:#3b2f2a!important;color:#fff!important}

/* ===== 버튼 폰트 톤 통일 ===== */
button,
a.reset-btn,
.container > div a {
    font-family: 'Pretendard','Poppins',Arial,sans-serif;
    letter-spacing: -0.2px;
    font-weight: 600;
}

/* ===== 블랙리스트(주의회원) 아이콘 ===== */
.warn-icon{
    margin-left:6px;
    color:#e53935;
    font-size:13px;
}
.warn-icon:hover{
    transform: scale(1.1);
}

/* ===== 아이콘 안내 ===== */
.legend-box{
    margin-top: 10px;
    display:flex;
    justify-content:flex-end;
    align-items:center;
    gap:10px;
    font-size:12px;
    color:#777;
}
.legend-box i{
    color:#e53935;
}

table#memberTbl{
    width:100%;
    table-layout: fixed; /* ★ 컬럼 폭 고정 */
}

/* 아이디 / 이메일 칸 폭 지정 */
table#memberTbl th:nth-child(2),
table#memberTbl td:nth-child(2){
    width: 170px;  /* 아이디 */
}

table#memberTbl th:nth-child(5),
table#memberTbl td:nth-child(5){
    width: 240px;  /* 이메일 */
}

/* 말줄임 처리 */
table#memberTbl td:nth-child(2),
table#memberTbl td:nth-child(5){
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}


</style>

<script type="text/javascript">
$(function(){
    // 회원 행 클릭 시 상세보기 이동
    $("table#memberTbl tr.memberInfo").on("click", function(){
        const userid = $(this).find(".userid").text().trim();
        const frm = document.memberOneDetailFrm;
        frm.userid.value = userid;
        frm.action = "<%=ctxPath%>/admin/memberDetail.sp";
        frm.method = "post";
        frm.submit();
    });
});
</script>

</head>

<body>

<!-- 고정 헤더 -->
<jsp:include page="../header2.jsp" />

<div class="container">

    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h3 class="font-weight-bold mb-4 text-dark">
            <i class="fas fa-users-cog" style="margin-right:8px;color:#6d4c41;"></i>
            회원 목록
        </h3>

        <a href="<%= ctxPath %>/admin/memberMain.sp"
           style="padding:8px 14px;background:#333;color:#fff;text-decoration:none;border-radius:4px;font-size:14px;">
            대시보드 이동
        </a>
    </div>

    <%-- ===============================
         회원 조회 영역
       =============================== --%>
    <div class="search-area">
        <h3>
            <i class="fas fa-search" style="margin-right:6px;color:#6d4c41;"></i>
            회원 조회
        </h3>
        <form action="<%=ctxPath%>/admin/memberList.sp" method="get">
            <select name="searchType">
                <option value="userid">아이디</option>
                <option value="name">이름</option>
            </select>
            <input type="text" name="searchWord" placeholder="검색어 입력">
            <button type="submit">
                <i class="fas fa-search"></i> 조회
            </button>

            <c:if test="${not empty param.searchWord}">
                <a href="<%=ctxPath%>/admin/memberList.sp" class="reset-btn">
                    전체 목록 보기
                </a>
            </c:if>
        </form>
    </div>

    <%-- ===============================
         회원 목록 테이블
       =============================== --%>
    <div class="legend-box">
    <span>
        <i class="fas fa-exclamation-triangle"></i>
        : 블랙리스트(주의회원)
    </span>
	</div>
    
    <table id="memberTbl">
        <thead>
            <tr>
                <th style="width:60px;">No</th>
                <th><i class="fas fa-user"></i>아이디</th>
                <th><i class="fas fa-id-card"></i>이름</th>
                <th><i class="fas fa-venus-mars"></i>성별</th>
                <th><i class="fas fa-envelope"></i>이메일</th>
                <th><i class="fas fa-calendar-alt"></i>가입일</th>
                <th><i class="fas fa-toggle-on"></i>상태</th>
            </tr>
        </thead>

        <tbody>
            <c:if test="${empty memberList}">
                <tr>
                    <td colspan="7">등록된 회원이 없습니다.</td>
                </tr>
            </c:if>

            <c:forEach var="m" items="${memberList}" varStatus="status">
                <tr class="memberInfo">
                    <td>${status.count}</td>
                    <td class="userid">${m.userid}</td>

                    <%-- 이름 + 블랙리스트 아이콘 --%>
                    <td>
                        ${m.name}

                        <c:if test="${not empty m.admin_memo 
                                     and (fn:contains(m.admin_memo,'블랙')
                                       or fn:contains(m.admin_memo,'주의')
                                       or fn:contains(m.admin_memo,'진상')
                                       or fn:contains(m.admin_memo,'환불')
                                       or fn:contains(m.admin_memo,'반품'))}">
                            <i class="fas fa-exclamation-triangle warn-icon"
                               title="주의회원"></i>
                        </c:if>
                    </td>

                    <td>
                        <c:choose>
                            <c:when test="${m.gender == '1'}">남</c:when>
                            <c:when test="${m.gender == '2'}">여</c:when>
                            <c:otherwise>미입력</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${m.email}</td>
                    <td>${m.registerday}</td>

                    <td>
                        <c:choose>
                            <c:when test="${m.status == 0}">
                                탈퇴
                            </c:when>

                            <c:when test="${m.status == 1 && m.idle == 1}">
                                휴면
                            </c:when>

                            <c:otherwise>
                                정상
                            </c:otherwise>
                        </c:choose>
                    </td>

                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- 페이지네이션 -->
    <ul class="pagination justify-content-center mt-4">
        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
            <a class="page-link" href="<%=ctxPath%>/admin/memberList.sp?pageNo=${currentPage - 1}">‹</a>
        </li>

        <c:forEach begin="1" end="${totalPage}" var="i">
            <li class="page-item ${currentPage == i ? 'active' : ''}">
                <a class="page-link" href="<%=ctxPath%>/admin/memberList.sp?pageNo=${i}">${i}</a>
            </li>
        </c:forEach>

        <li class="page-item ${currentPage == totalPage ? 'disabled' : ''}">
            <a class="page-link" href="<%=ctxPath%>/admin/memberList.sp?pageNo=${currentPage + 1}">›</a>
        </li>
    </ul>

</div>

<!-- 회원 더미 추가 버튼 -->
<a href="<%=ctxPath%>/admin/dummyMember50.sp"
   onclick="return confirm('더미 회원 50명을 생성하시겠습니까?');"
   style="display:none; padding:8px 14px;background:#5d4037;color:#fff;border-radius:4px;text-decoration:none;">
   더미회원 50명 생성(비번 1234 통일)
</a>


<form name="memberOneDetailFrm">
    <input type="hidden" name="userid" />
</form>

<!-- 고정 푸터 -->
<jsp:include page="../footer2.jsp" />

</body>
</html>
