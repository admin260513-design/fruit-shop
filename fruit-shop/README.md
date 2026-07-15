# 과일 상점 🍉

#### 📋프로젝트 소개
JWT 기반 인증을 적용한 과일 쇼핑몰 애플리케이션이다. 사용자는 회원가입 및 로그인을 통해 인증을 완료한 후 판매자로 상품을 등록할 수 있으며, 구매자로 상품을 장바구니에 담아 주문 및 구매할 수 있도록 구현하였다.


#### 🎯 학습 목표
- Spring Security + JWT 인증 흐름 이해
- REST API 설계
- 장바구니 및 주문 로직 구현
- React 연동 경험
- 협업 및 Git 브랜치 전략 경험

#### 🚀 주요 기능
- 회원가입
- 로그인
- 상품 CRUD
- 장바구니
- 주문

---
#### 회원가입

회원 정보를 등록하고 비밀번호를 BCrypt로 암호화하여 저장하도록 구현
- 아이디 중복 검사
- 비밀번호 암호화 저장
- 회원 정보 DB 저장

---
#### 로그인

Spring Security와 JWT를 활용한 사용자 인증 기능 구현
- 사용자 인증
- JWT Access Token 발급
- 인증된 사용자 권한 검증

---
#### 상품 등록

인증된 사용자가 상품을 등록, 조회, 수정, 삭제할 수 있도록 CRUD 기능 구현
- 상품 등록
- 상품 조회
- 상품 수정
- 상품 삭제
- 상품 이미지(URL) 관리

---
#### 장바구니

로그인한 사용자가 원하는 상품을 장바구니에 담고 관리할 수 있도록 구현
- 상품 추가
- 수량 변경
- 상품 삭제
- 장바구니 조회

---

#### 주문

상품 상세페이지 또는 장바구니에 담긴 상품을 주문하고 주문 내역을 관리할 수 있도록 구현

- 주문 생성
- 주문 정보 저장
- 주문 내역 조회

---

### 🛠️ 기술 스택

##### 백엔드
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![SpringBoot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) ![SpringBoot](https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens) ![Mysql](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mariadb&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
---
##### 프론트
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB) ![ChatGPT](https://img.shields.io/badge/chatGPT-74aa9c?style=for-the-badge&logo=openai&logoColor=white)
---
##### 협업
![GitLab](https://img.shields.io/badge/gitlab-FC6D26.svg?style=for-the-badge&logo=gitlab&logoColor=white)

---

### 프로젝트 구조
### BE

```fruitshop
└─📦 src
  ├─📂 test
  └─📂 main
    ├─📂 resources
    └─📂 java
      └─📂 org
        └─ 📂 example
          └─ 📂 fruitshop
            ├─ 📂 config
            ├─ 📂 controller
            ├─ 📂 dto
            ├─ 📂 entity
            ├─ 📂 repository
            ├─ 📂 service
            └─ 📂 util
```

---

### API 구조
<img width="410" height="315" alt="image" src="https://github.com/user-attachments/assets/4cb20a10-3556-441c-937b-18639d9cb064" />
<img width="309" height="250" alt="image" src="https://github.com/user-attachments/assets/d8cb0d5a-a411-4c4f-a363-94e3afe48983" />

