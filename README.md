# 👓 안경 쇼핑몰 플랫폼 (E-Commerce Web Service)
> **세미 프로젝트 (팀 프로젝트 / MVC 기반 웹 서비스)**
>
> 다양한 안경 상품을 조회하고 구매할 수 있는 **안경 쇼핑몰 웹 서비스**입니다.  
> 상품 조회, 장바구니, 주문, 관리자 상품 관리 기능을 중심으로  
> **Spring MVC 기반의 CRUD 웹 서비스 구조**를 설계하고 구현했습니다.

<br>

## 1. 📅 프로젝트 기간
- 2026.01.xx ~ 2026.02.xx

<br>

## 2. 🛠 기술 스택

### 💻 Backend
<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring%20MVC-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
<img src="https://img.shields.io/badge/JSP-007396?style=for-the-badge&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/Servlet-000000?style=for-the-badge&logo=apachetomcat&logoColor=white"/>

### 🗄️ Database
<img src="https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white"/>

### 🌐 Frontend
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"/>
<img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"/>
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"/>
<img src="https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white"/>

<br>

## 3. 👤 담당 역할 및 기여도 (My Role)

- **상품 조회 및 상세 페이지 구현**
  - 안경 상품 리스트 조회 기능 구현
  - 상품 상세 페이지에서 상품명, 가격, 이미지, 옵션 정보 출력
  - Controller → Service → DAO → DB → JSP View 흐름 구현

- **장바구니 및 주문 흐름 구현**
  - 사용자가 선택한 상품을 장바구니에 담는 기능 구현
  - 장바구니 상품 수량 변경 및 삭제 기능 구현
  - 주문 페이지로 상품 정보 전달 및 주문 데이터 저장 흐름 구현

- **관리자 상품 관리 기능 구현**
  - 관리자 페이지에서 상품 등록, 수정, 삭제 기능 구현
  - 상품 이미지, 가격, 재고, 카테고리 정보를 관리할 수 있도록 구성

- **DB 설계 및 데이터 연동**
  - 상품, 회원, 장바구니, 주문 관련 테이블 설계 참여
  - MyBatis Mapper를 활용한 SQL 작성 및 데이터 조회/저장 처리

<br>

## 4. 🚀 핵심 기능

### ✅ 상품 목록 및 상세 조회

- 사용자는 안경 상품 목록을 조회할 수 있습니다.
- 상품 클릭 시 상세 페이지로 이동하여 상품 정보, 가격, 이미지 등을 확인할 수 있습니다.
- 상품 데이터는 DB에서 조회 후 JSP 화면에 출력됩니다.

**백엔드 흐름 요약**

```text
사용자 요청
 → Controller
 → Service
 → DAO
 → Mapper SQL
 → DB 조회
 → JSP View 반환
<br>
✅ 장바구니 기능
사용자는 원하는 상품을 장바구니에 담을 수 있습니다.
장바구니에서 상품 수량 변경 및 삭제가 가능합니다.
회원별 장바구니 데이터를 DB에 저장하여 관리합니다.

백엔드 흐름 요약

상품 선택
 → 장바구니 추가 요청
 → 회원 정보 확인
 → 상품 정보 조회
 → 장바구니 테이블 저장
 → 장바구니 화면 출력
<br>
✅ 주문 처리 기능
장바구니에 담긴 상품을 기반으로 주문 페이지로 이동합니다.
주문자 정보, 상품 정보, 총 금액을 확인한 뒤 주문 데이터를 저장합니다.
주문 정보와 주문 상세 정보를 분리하여 관리할 수 있도록 설계했습니다.

백엔드 흐름 요약

장바구니 상품 선택
 → 주문 페이지 이동
 → 주문 정보 입력
 → 주문 데이터 생성
 → ORDER / ORDER_DETAIL 테이블 저장
 → 주문 완료 화면 반환
<br>
✅ 관리자 상품 관리
관리자는 상품을 등록, 수정, 삭제할 수 있습니다.
상품명, 가격, 카테고리, 이미지, 재고 정보를 관리합니다.
사용자 화면과 관리자 화면을 분리하여 기능을 구성했습니다.

백엔드 흐름 요약

관리자 요청
 → 상품 등록/수정/삭제 Controller
 → Service 검증
 → DAO 호출
 → DB 반영
 → 관리자 상품 목록 갱신
<br>
5. 📂 프로젝트 구조
src/main/java/com/shop
 ├── controller     # 사용자/관리자 요청 처리
 ├── service        # 비즈니스 로직 처리
 ├── dao            # DB 접근 로직
 ├── dto            # 데이터 전달 객체
 └── common         # 공통 기능 및 유틸

src/main/webapp/WEB-INF/views
 ├── product        # 상품 목록 / 상세 페이지
 ├── cart           # 장바구니 페이지
 ├── order          # 주문 / 주문 완료 페이지
 ├── admin          # 관리자 상품 관리 페이지
 └── member         # 회원 관련 페이지
<br>
6. 🧩 주요 구현 포인트
✅ MVC 패턴 기반 계층 분리

세미 프로젝트에서는 Spring MVC 구조를 기반으로
Controller, Service, DAO 계층을 분리하여 구현했습니다.

Controller : 사용자의 요청을 받고 응답 화면을 결정
Service    : 비즈니스 로직 처리
DAO        : DB 접근 처리
Mapper     : SQL 실행
JSP        : 화면 출력

이를 통해 화면 처리와 비즈니스 로직, 데이터 접근 로직을 분리하여
유지보수하기 쉬운 구조로 구현하고자 했습니다.

<br>
✅ JSP 기반 동적 화면 처리

DB에서 조회한 상품 데이터를 JSP로 전달하고,
JSTL과 EL 표현식을 활용하여 화면에 동적으로 출력했습니다.

DB 상품 데이터 조회
 → Model 객체에 데이터 저장
 → JSP에서 반복문으로 상품 목록 출력
<br>
✅ CRUD 기능 구현 경험

상품, 장바구니, 주문 기능을 구현하면서
웹 서비스의 기본이 되는 CRUD 흐름을 경험했습니다.

Create : 상품 등록, 장바구니 추가, 주문 생성
Read   : 상품 목록 조회, 상품 상세 조회, 장바구니 조회
Update : 상품 수정, 장바구니 수량 변경
Delete : 상품 삭제, 장바구니 상품 삭제
<br>
7. ⚠️ 한계점 및 개선 방향
한계점
세션 기반 인증 구조로 인해 확장성에 한계가 있었습니다.
동시 주문 상황에서 재고 정합성을 보장하는 로직이 부족했습니다.
대용량 데이터 조회 시 성능 최적화에 대한 고려가 부족했습니다.
화면 중심 구현이 많아 백엔드 구조 설계에 대한 깊이가 부족했습니다.
<br>
개선 방향
인증 방식을 JWT 기반 구조로 개선
주문/재고 처리 시 트랜잭션과 Lock을 활용한 데이터 정합성 확보
인덱스 설계 및 SQL 튜닝을 통한 조회 성능 개선
REST API 기반 구조로 전환하여 프론트엔드와 백엔드 역할 분리
<br>
8. 🧠 프로젝트 회고

세미 프로젝트를 통해 Spring MVC 기반 웹 서비스의 기본 구조를 이해할 수 있었습니다.
특히 Controller, Service, DAO, JSP로 이어지는 요청 처리 흐름을 직접 구현하면서
웹 애플리케이션이 DB와 화면을 어떻게 연결하는지 경험했습니다.

다만 세미 프로젝트는 CRUD 중심의 기능 구현에 집중했기 때문에
보안, 성능, 동시성 처리 측면에서는 한계가 있었습니다.

이 경험을 바탕으로 파이널 프로젝트에서는
JWT 인증, 결제 검증, DB Lock 기반 동시성 제어, 배포 자동화 등
보다 백엔드 중심적인 구조를 적용하고자 했습니다.
