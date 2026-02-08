<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../header2.jsp" />

<style>
/* ================================
   ADMIN CLAIM – BASE
================================ */
:root{
    --carin-black:#1a1a1a;
    --carin-gray:#8c8c8c;
    --carin-light-gray:#f7f7f7;
    --carin-border:#e6e6e6;
    --carin-point:#bcaaa4;
    --success:#4caf50;
    --danger:#e53935;
    --exchange:#9e9e9e;
}

/* 전체 래퍼 */
.carin-admin-wrap{
    max-width:1400px;
    margin:0 auto;
    padding:60px 24px 120px;
    background:#fff;
    font-family:'Pretendard',sans-serif;
}

/* 타이틀 */
.page-title{
    font-size:20px;
    font-weight:600;
    letter-spacing:.12em;
    text-align:center;
    margin-bottom:60px;
}

/* ================================
   SUMMARY
================================ */
.summary-section{
    display:flex;
    gap:24px;
    justify-content:center;
    margin-bottom:70px;
}

.summary-item{
    position:relative;
    background:#fafafa;
    border-radius:14px;
    padding:28px 28px;
    min-width:240px;
    box-shadow:0 6px 18px rgba(0,0,0,.06);
}

.summary-item::before{
    content:"";
    position:absolute;
    left:0;
    top:0;
    width:6px;
    height:100%;
    border-radius:14px 0 0 14px;
    background:var(--carin-point);
}

.summary-item .label{
    font-size:11px;
    letter-spacing:.14em;
    color:#777;
    margin-bottom:10px;
}

.summary-item .count{
    font-size:28px;
    font-weight:700;
    color:#2f2b2a;
}

/* ================================
   TABLE
================================ */
.return-table{
    width:100%;
    border-collapse:separate;
    border-spacing:0 14px;
    font-size:13px;
}

.return-table thead th{
    font-size:12px;
    color:var(--carin-gray);
    font-weight:600;
    padding:12px;
    text-align:center;
}

.return-table tbody tr{
    background:#fff;
    border-radius:12px;
    outline:1px solid #ececec;
    box-shadow:0 2px 8px rgba(0,0,0,.05);
}

.return-table td{
    padding:18px 14px;
    text-align:center;
    vertical-align:middle;
}

/* ================================
   REQUEST TYPE
================================ */
.badge{
    display:inline-flex;
    align-items:center;
    gap:6px;
    padding:6px 12px;
    font-size:11px;
    border-radius:999px;
    font-weight:600;
}

.badge.cancel{background:#fdecea;color:var(--danger)}
.badge.return{background:#f3edea;color:var(--carin-point)}
.badge.exchange{background:#f0f0f0;color:var(--exchange)}

.badge.cancel::before{content:"❌"}
.badge.return::before{content:"↩"}
.badge.exchange::before{content:"🔁"}

/* ================================
   STATUS
================================ */
.badge.request{background:#f5f5f5;color:#777}
.badge.wait{background:#f3edea;color:var(--carin-point)}
.badge.done{background:#edf7ed;color:var(--success)}
.badge.reject{background:#f5f5f5;color:#999}

/* ================================
   BUTTON
================================ */
.btn-group{
    display:flex;
    flex-direction:column;
    gap:6px;
}

.btn-admin{
    font-size:11px;
    padding:8px 0;
    border-radius:4px;
    border:1px solid #d6d6d6;
    background:#fff;
    color:#333;
    cursor:pointer;
}

.btn-approve{
    background:#5d4037;
    border-color:#5d4037;
    color:#fff;
}

.btn-complete{
    background:#2e7d32;
    border-color:#2e7d32;
    color:#fff;
}
</style>

<script>
function completeClaim(odrdetailno){
    if(!confirm("해당 요청을 처리완료 하시겠습니까?\n(결제취소/배송취소/재고반영 진행)")) return;
    location.href =
        "${pageContext.request.contextPath}/admin/claimComplete.sp?odrdetailno=" + odrdetailno;
}
</script>

<c:if test="${not empty sessionScope.alertMsg}">
    <script>
        alert("${sessionScope.alertMsg}");
    </script>
    <c:remove var="alertMsg" scope="session"/>
</c:if>


<div class="carin-admin-wrap">

    <h2 class="page-title">취소 / 반품 / 교환 요청 관리</h2>

    <div class="summary-section">
        <div class="summary-item">
            <div class="label">전체 요청</div>
            <div class="count">${not empty claimList ? claimList.size() : 0}</div>
        </div>
        <div class="summary-item">
            <div class="label">처리 대기</div>
            <div class="count">${pendingCount}</div>
        </div>
    </div>

    <table class="return-table">
        <thead>
            <tr>
                <th>주문번호</th>
                <th>상품명</th>
                <th>수량</th>
                <th>요청유형</th>
                <th>사유</th>
                <th>상태</th>
                <th>매니지먼트</th>
            </tr>
        </thead>

        <tbody>
        <c:forEach var="c" items="${claimList}">
            <tr>
                <td>${c.odrCode}</td>

                <td style="text-align:left;padding-left:20px;">
                    ${c.productName}
                </td>

                <td>${c.odrQty}</td>

                <td>
                    <c:choose>
                        <c:when test="${c.claimType == 'CANCEL'}">
                            <span class="badge cancel">취소</span>
                        </c:when>
                        <c:when test="${c.claimType == 'RETURN'}">
                            <span class="badge return">반품</span>
                        </c:when>
                        <c:when test="${c.claimType == 'EXCHANGE'}">
                            <span class="badge exchange">교환</span>
                        </c:when>
                    </c:choose>
                </td>

                <td style="font-size:12px;color:#777;">
                    ${c.claimReason}
                </td>

                <td>
                    <c:choose>
                        <c:when test="${c.claimStatus == 'REQUEST'}">
                            <span class="badge request">요청됨</span>
                        </c:when>
                        <c:when test="${c.claimStatus == 'APPROVED'}">
                            <span class="badge wait">처리대기</span>
                        </c:when>
                        <c:when test="${c.claimStatus == 'COMPLETED'}">
                            <span class="badge done">처리완료</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge reject">반려</span>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <div class="btn-group">
                        <c:choose>
                            <c:when test="${c.claimStatus == 'REQUEST'}">
                                <button class="btn-admin btn-approve"
                                    onclick="location.href='${pageContext.request.contextPath}/admin/claimApprove.sp?odrdetailno=${c.odrDetailNo}'">
                                    승인
                                </button>
                                <button class="btn-admin"
                                    onclick="location.href='${pageContext.request.contextPath}/admin/claimReject.sp?odrdetailno=${c.odrDetailNo}'">
                                    반려
                                </button>
                            </c:when>

                            <c:when test="${c.claimStatus == 'APPROVED'}">
                                <button class="btn-admin btn-complete"
                                    onclick="completeClaim('${c.odrDetailNo}')">
                                    처리완료
                                </button>
                            </c:when>

                            <c:otherwise>
                                -
                            </c:otherwise>
                        </c:choose>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="../footer2.jsp" />
