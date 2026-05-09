# 👓 SISEON — 안경 판매 쇼핑몰

> **Java Servlet/JSP 기반 MVC 패턴 E-Commerce 플랫폼**  
> 사용자 인증 흐름 전반과 관리자 백오피스 운영 기능을 직접 설계·구현한 팀 프로젝트입니다.

<br>

## 📌 프로젝트 개요

| 항목 | 내용 |
|---|---|
| 진행 기간 | 2025.11.28 ~ 2025.12.26 (4주) |
| 팀 구성 | 4인 팀 프로젝트 (기여도 25%) |
| 아키텍처 | MVC Pattern (프레임워크 미사용, 직접 구현) |
| Backend | Java, Servlet, JSP |
| Server | Apache Tomcat |
| Database | Oracle (순수 JDBC — MyBatis, JPA 미사용) |
| Frontend | HTML, CSS, JavaScript, jQuery, Bootstrap |
| 외부 API | Naver/Kakao OAuth2, CoolSMS, Chart.js |
| 보안 | SHA-256 (비밀번호), AES-256 (이메일·전화번호), Gmail SMTP (OTP) |

<br>

## 🔗 링크

- **PPT (코드 설명 / 스토리보드 / 다이어그램)**: [Canva 바로가기](https://www.canva.com/design/DAHBM_4bkgg/9G5BpjgAa5WxccCtyKX6eA/edit)
- **시연 영상**: [YouTube 바로가기](https://www.youtube.com/watch?v=UBd21oA-Vpc)
- **GitHub**: [lhk9311/SemiProject](https://github.com/lhk9311/SemiProject)

<br>

## 👤 담당 역할 (My Contribution)

### 🔐 회원 인증 도메인

| 기능 | 구현 내용 |
|---|---|
| 회원가입 | AJAX 아이디 중복 검사 + 트랜잭션 기반 회원/배송지 테이블 동시 INSERT |
| 로그인 / 로그아웃 | HttpSession 기반 인증 상태 유지, 관리자/일반 회원 분기 처리 |
| 소셜 로그인 | Naver/Kakao OAuth2 — 기존 회원 자동 로그인, 신규 회원 추가 정보 입력 플로우 |
| 아이디 저장 | Session(서버 인증)과 localStorage(클라이언트 상태) 역할 분리 |
| 비밀번호 찾기 | Gmail SMTP 인증코드 발송 → 세션 기반 재설정 권한 부여 → SHA-256 암호화 저장 |
| 휴면 계정 해제 | 로그인 시 idle 값 확인 → idle=0 UPDATE → 재로그인 유도 |
| 아이디 찾기 / 회원탈퇴 | name·email 조회 + session.invalidate() 기반 로그아웃 처리 |

### 📦 마이페이지 — 주문 / 클레임

| 기능 | 구현 내용 |
|---|---|
| 주문 목록 조회 | 상태(status) 필터 + 기간(range) 파라미터 서버 계산 + 페이징 처리 + JSTL 렌더링 |
| 주문 상세 조회 | `tbl_order`, `tbl_order_detail`, `tbl_product` JOIN 조회 + OrderDetailDTO 매핑 |
| 클레임 신청 | 주문상품(odrdetailno) 단위 취소/교환/환불 팝업 (window.open) + radio 버튼 유효성 검사 |

### 🛠️ 백오피스 — 관리자 기능

| 기능 | 구현 내용 |
|---|---|
| 클레임 관리 | REQUEST → APPROVED(처리대기) → COMPLETED(처리완료) 3단계 상태 흐름 설계 |
| 클레임 반려 | 반려 사유 입력 폼 → REJECTED 상태 업데이트 + 사용자 화면 모달 반영 |
| 회원 목록 / 검색 | 페이징(startRno~endRno) + searchType/searchWord 파라미터 기반 동적 쿼리 |
| 회원 상세 조회 | tbl_member + tbl_grade JOIN 조회 → 상세 정보 렌더링 |
| 문자 발송 | CoolSMS API + AJAX 비동기 전송 → JSON 응답 처리 |
| 일괄 휴면 해제 | 체크박스 선택 → userid 배열 서버 전달 → idle 일괄 UPDATE |
| 블랙리스트 관리 | admin_memo 200자 서버사이드 컷(Validation) + 키워드 감지 아이콘 표시 |
| 관리자 대시보드 | DB `COUNT(*)` 집계 + Chart.js 최근 7일 가입 추이 라인 차트 시각화 |

<br>

## 🔍 트러블슈팅

### 01. 주문 이력 데이터 보존 — 배송지 스냅샷 적용

**문제**  
배송지 FK 참조 구조에서 회원이 주소를 변경하면, 이미 완료된 과거 주문의 배송지 정보까지 함께 변경되는 문제 발생.

**원인**  
주문 상세 화면에서 배송지를 `tbl_member`의 주소 컬럼을 직접 참조하는 구조였기 때문. 주문 시점의 데이터가 아닌 현재 회원 정보가 출력됨.

**해결**  
주문 테이블(`tbl_order_detail`)에 배송지 컬럼 (`postcode`, `address`, `detailaddress`, `extraaddress`)을 추가하고, 주문 INSERT 시점에 배송지 데이터를 복사해 스냅샷으로 저장. 이후 회원 정보 변경과 무관하게 주문 이력 데이터 일관성 확보.

---

### 02. 클레임 승인 · 재고 반영 시점 분리

**문제**  
초기 설계에서 관리자가 승인 버튼을 클릭하는 즉시 결제 취소 및 재고 복구가 발생. 잘못 승인했을 경우 되돌리기 어려운 구조.

**해결**  
클레임 처리를 3단계로 분리.

```
REQUEST (사용자 신청)
  → APPROVED (관리자 승인 — 처리 대기 상태)
  → COMPLETED (관리자 처리완료 — 재고 복구 + 결제 취소 실제 실행)
```

승인(APPROVED)과 실제 처리(COMPLETED)를 별도 단계로 나눔으로써, 관리자가 한 번 더 확인 후 재고·결제 상태를 변경하도록 개선. 주문 명확성 및 재고 정합성 향상.

<br>

---

## [배포] 정적 호스팅(Netlify) 환경에서의 JSP 구동 실패 분석 - 1

**문제**
: Netlify를 통해 프로젝트 배포 시 '404 Page Not Found' 에러가 발생하며 사이트가 정상적으로 열리지 않음.

**원인 분석**
: Netlify는 HTML/JS 기반의 **정적 호스팅** 서비스이나, 본 프로젝트는 **Tomcat(WAS)**과 
**JVM** 환경이 필요한 동적 웹 애플리케이션(JSP/Servlet)임을 파악.

**해결 방안**
: 정적/동적 서버의 차이를 기술적으로 분석하고, 향후 AWS EC2 등 서버 사이드 스크립트 실행이 가능한 환경으로의 이전 계획 수립.

---

## [배포] Vultr + Tomcat + Oracle XE(Docker) 환경 배포 - 2

**문제**
: Netlify는 정적 호스팅 기반 서비스이기 때문에 JSP/Servlet 기반 애플리케이션 실행이 불가능했음.

**해결**
: Vultr VPS 환경에서 Ubuntu 서버를 직접 구성하고, Tomcat + Oracle XE(Docker) 기반으로 프로젝트를 재배포함.

**배포 환경**
- Vultr Ubuntu 24.04 LTS
- Apache Tomcat 10
- Oracle XE 18c (Docker)
- Nginx Reverse Proxy
- Gabia Domain
- Certbot HTTPS(SSL)

**구성 흐름**

```text
[ Client ]
     ↓ HTTPS
[ Nginx ]
     ↓ 127.0.0.1:8080
[ Tomcat 10 ]
     ↓ JDBC
[ Oracle XE Docker ]
```
<br>

### 📚 배포 경험 및 학습 내용

- Vultr VPS 환경에서 Ubuntu 서버 직접 구성
- Tomcat 수동 설치 및 WAR 배포 경험
- Docker 기반 Oracle XE 컨테이너 운영
- Nginx Reverse Proxy 구성 및 포트 포워딩 설정
- Gabia 도메인 연결 및 DNS 설정
- Certbot 기반 HTTPS(SSL) 인증서 적용
- `context.xml` 기반 JDBC DataSource 설정
- Linux(Ubuntu) 환경에서 방화벽(`ufw`) 및 서버 프로세스 관리 경험

<br>

### 🔧 Trouble Shooting

| 문제 | 해결 |
|---|---|
| Oracle XE 21c 컨테이너 메모리 부족 (`Exited 224`) | `oracle-xe:18-slim` 경량 이미지로 변경 |
| SQL Import 시 `ORA-00942` 발생 | 애플리케이션 전용 유저 생성 후 Import |
| 일부 테이블 누락 | `DBMS_METADATA.GET_DDL` 기반 수동 생성 |
| Netlify 배포 실패 | VPS 기반 동적 서버 환경으로 전환 |

## ⚠️ 한계점

- **클레임 처리완료 DAO**: `tbl_order_detail`, `tbl_order`, `tbl_product` 순차 UPDATE 시 트랜잭션 미적용 → 부분 성공 리스크 존재. `conn.setAutoCommit(false)` 기반 트랜잭션 처리 적용 필요.

<br>

## 📁 프로젝트 구조 (담당 영역 중심)

```
src/main/java/hk/
 ├── member/
 │   ├── controller/
 │   │   ├── RegisterController.java       # 회원가입
 │   │   ├── LoginController.java          # 로그인
 │   │   ├── IdReleaseController.java      # 휴면 해제
 │   │   ├── NaverLoginStartController.java
 │   │   ├── NaverCallbackController.java
 │   │   ├── KakaoLoginStartController.java
 │   │   ├── KakaoCallbackController.java
 │   │   ├── SocialJoinEndController.java  # 소셜 신규 회원 추가정보
 │   │   ├── PwdFindController.java
 │   │   ├── VerifyCertificationController.java
 │   │   ├── PwdResetController.java
 │   │   └── PwdResetEndController.java
 │   └── model/
 │       ├── MemberDAO.java (interface)
 │       └── MemberDAO_Imple.java
 ├── order/
 │   └── controller/
 │       ├── OrderListController.java      # 주문 목록 (페이징 + 필터)
 │       ├── OrderDetailController.java    # 주문 상세
 │       ├── OrderCancelPopupController.java
 │       └── OrderCancelRequestController.java
 ├── admin/
 │   └── controller/
 │       ├── AdminClaimListController.java
 │       ├── AdminClaimApproveController.java
 │       ├── AdminClaimRejectController.java
 │       ├── AdminClaimCompleteController.java
 │       ├── AdminMemberListController.java
 │       ├── AdminMemberDetailController.java
 │       ├── AdminMemberSmsSendController.java
 │       ├── IdleMemberListController.java
 │       ├── IdleMemberReleaseController.java
 │       ├── AdminMemoUpdateController.java
 │       └── AdminMemberMainController.java  # 대시보드 + Chart.js
 └── login/
     └── controller/
         ├── PwdFindController.java
         └── GoogleMailController.java     # Gmail SMTP
```

<br>



---

## 메인 화면

<img src="./SemiProject/images/SISEON.png" width="800"/>
