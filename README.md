# 👓 안경 쇼핑몰 플랫폼: SISEON (E-Commerce)
> **사용자 경험과 데이터 무결성을 고려한 MVC 기반 이커머스 웹 서비스**
> 
> 단순히 물건을 파는 기능을 넘어, **배송지 정보의 이력 관리**와 **재고 정합성** 등 실제 서비스에서 발생할 수 있는 문제들을 고민하며 개발한 프로젝트입니다.

<br>

## 1. 📅 프로젝트 개요
- **진행 기간**: 2025.11.28 ~ 2025.12.26 (4주)
- **개발 인원**: 4명 (팀 프로젝트 / 기여도 25%)
- **주요 목표**: MVC 패턴의 명확한 이해 및 확장 가능한 백엔드 구조 설계

<br>

## 2. 🛠 기술 스택 (Tech Stack)

### 💻 Backend
<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white"/> <img src="https://img.shields.io/badge/Servlet/JSP-007396?style=for-the-badge&logo=java&logoColor=white"/> <img src="https://img.shields.io/badge/Apache%20Tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black"/>

### 🗄️ Database
<img src="https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white"/> <img src="https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=mybatis&logoColor=white"/>

### 🌐 Frontend
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"/> <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"/> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"/> <img src="https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white"/> <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white"/>

### 🔌 API & Etc
- **결제**: iamport (포트원) 결제 API
- **인증**: CoolSMS (문자 인증), OAuth 2.0 (카카오/구글 소셜 로그인)
- **기타**: Chart.js (통계 데이터 시각화), ajax, JSON

<br>

## 3. 👤 담당 역할 및 핵심 기여 (My Role)

- **상품 관리 및 전시 (CRUD) 핵심 로직 구현**
  - 상품 등록, 수정, 삭제 및 카테고리별 동적 필터링 기능 구현
  - `Controller` ↔ `Service` ↔ `DAO` 계층 분리를 통해 비즈니스 로직의 독립성 확보
- **장바구니 및 주문 프로세스 설계**
  - 회원별 장바구니 데이터의 DB 연동 및 실시간 수량 변경 로직 구현
  - 주문서 생성 및 주문 상세 내역(`ORDER_DETAIL`)의 트랜잭션 처리
- **외부 모듈 연동 및 검증**
  - **iamport API** 연동을 통한 실시간 결제 프로세스 구현 및 결제 위변조 검증 로직 적용

<br>

## 🔍 핵심 트러블슈팅 (Troubleshooting)

### 01. 데이터 일관성: 배송지 정보 스냅샷(Snapshot) 적용
- **문제**: 주문 완료 후 사용자가 마이페이지에서 주소를 변경하면, **과거 주문 내역의 배송지까지 수정**되어 배송 사고 데이터가 발생하는 문제 확인.
- **해결**: `MEMBER` 테이블의 주소를 참조하지 않고, 주문 시점에 주소 정보를 `ORDER` 테이블에 직접 삽입(역정규화)하여 **결제 당시의 배송 정보가 보존**되도록 개선.

### 02. 재고 관리: 승인 시점과 재고 차감의 분리
- **문제**: 결제와 동시에 재고가 차감될 경우, 관리자의 주문 취소나 반려 시 재고 복구 로직이 복잡해지고 데이터 정합성이 깨질 위험 존재.
- **해결**: 트랜잭션 단계를 '승인 대기'와 '출고 처리'로 분리. 실제 물류가 이동하는 **최종 승인 시점에 재고가 차감**되도록 설계하여 정합성 확보.

<br>

## 📁 프로젝트 구조 (Architecture)
```text
src/main/java/com/shop
 ├── controller  # HTTP 요청 제어 및 View 매핑
 ├── service     # 비즈니스 로직 및 트랜잭션 관리
 ├── dao         # DB 접근 로직 (MyBatis Mapper 호출)
 ├── dto/vo      # 계층 간 데이터 전송 객체
 └── common      # 공통 필터, 파일 업로드 유틸리티
