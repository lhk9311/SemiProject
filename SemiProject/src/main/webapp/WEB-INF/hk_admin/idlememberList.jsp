<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String ctxPath = request.getContextPath(); %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>관리자 | 휴면회원 관리</title>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">

<style>
body{font-family:'Pretendard',Arial,sans-serif;background:#f7f6f3;color:#333}
.container{width:1100px;margin:60px auto;background:#fff;padding:40px;border-radius:4px;box-shadow:0 4px 12px rgba(0,0,0,.06)}
h3{font-size:18px;font-weight:700;letter-spacing:-.3px;color:#2f2b2a;margin-bottom:0}
.container>div a{background:#3e3a39!important;transition:.2s}
.container>div a:hover{background:#2f2b2a!important}

table{width:100%;border-collapse:collapse;margin-top:20px;font-size:14px}
thead th{background:#f2f1ee;font-weight:600;color:#555;padding:14px 10px;border-bottom:2px solid #ddd}
tbody td{padding:14px 10px;border-bottom:1px solid #eee;color:#444}
tbody tr:hover{background:#faf9f7}
tbody tr td[colspan]{padding:30px 0;color:#777;font-size:14px}

.btn-area{display:flex;justify-content:space-between;align-items:center;margin-top:18px}
.btn{
    padding:10px 18px;border:none;border-radius:3px;
    background:#6d4c41;color:#fff;font-weight:600;cursor:pointer;
}
.btn:hover{background:#5d4037}

.btn-sub{
    padding:10px 18px;border-radius:3px;
    background:#3e3a39;color:#fff;text-decoration:none;font-weight:600;
}
.btn-sub:hover{background:#2f2b2a}

.badge{
    display:inline-block;
    padding:5px 10px;
    border-radius:20px;
    font-size:12px;
    font-weight:700;
    background:#f1ebe6;
    color:#5d4037;
}
</style>

<script type="text/javascript">
$(function(){

    // 전체선택
    $("#allCheck").on("change", function(){
        const bool = $(this).prop("checked");
        $("input:checkbox[name='userid']").prop("checked", bool);
    });

    // 개별 체크 해제 시 전체선택 해제
    $(document).on("change", "input:checkbox[name='userid']", function(){
        const total = $("input:checkbox[name='userid']").length;
        const checked = $("input:checkbox[name='userid']:checked").length;

        $("#allCheck").prop("checked", total === checked);
    });

    // 휴면 해제
    $("#btnRelease").on("click", function(){

        const checkedCnt = $("input:checkbox[name='userid']:checked").length;

        if(checkedCnt == 0){
            alert("휴면 해제할 회원을 선택하세요.");
            return;
        }

        if(!confirm("선택한 회원 " + checkedCnt + "명의 휴면을 해제하시겠습니까?")){
            return;
        }

        document.releaseFrm.submit();
    });

});
</script>

</head>

<body>

<jsp:include page="../header2.jsp" />

<div class="container">

    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h3>
            <i class="fas fa-user-clock" style="margin-right:8px;color:#6d4c41;"></i>
            휴면회원 관리
            <span class="badge">휴면회원만 표시</span>
        </h3>

        <a href="<%= ctxPath %>/admin/memberMain.sp" class="btn-sub">
            대시보드 이동
        </a>
    </div>

    <div class="btn-area">
        <div>
            <button type="button" class="btn" id="btnRelease">
                <i class="fas fa-unlock"></i> 선택 휴면 해제
            </button>
        </div>

        <div style="font-size:13px;color:#777;">
            총 <b>${empty idleCount ? 0 : idleCount}</b>명
        </div>
    </div>

    <form name="releaseFrm" method="post" action="<%=ctxPath%>/admin/idleMemberRelease.sp">
        <table id="memberTbl">
            <thead>
                <tr>
                    <th style="width:60px;">
                        <input type="checkbox" id="allCheck" />
                    </th>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>가입일</th>
                    <th>상태</th>
                </tr>
            </thead>

            <tbody>
                <c:if test="${empty idleMemberList}">
                    <tr>
                        <td colspan="6">휴면 회원이 없습니다.</td>
                    </tr>
                </c:if>

                <c:forEach var="m" items="${idleMemberList}" varStatus="status">
                    <tr>
                        <td>
                            <input type="checkbox" name="userid" value="${m.userid}" />
                        </td>
                        <td>${m.userid}</td>
                        <td>${m.name}</td>
                        <td>${m.email}</td>
                        <td>${m.registerday}</td>
                        <td>휴면</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </form>

</div>

<jsp:include page="../footer2.jsp" />

</body>
</html>
