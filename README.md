

# Spring Boot 게시판 웹 애플리케이션

이 프로젝트는 **Spring Boot**를 사용하여 개발된 **회원가입**, **게시판**, **댓글** 기능이 포함된 웹 애플리케이션입니다. 사용자들은 회원 가입 후, 게시글을 작성하고 댓글을 달 수 있습니다.


---
- <img src="https://img.shields.io/badge/Framework-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/2.7.18-515151?style=for-the-badge">
- <img src="https://img.shields.io/badge/Language-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/java-%23ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"><img src="https://img.shields.io/badge/11-515151?style=for-the-badge">

---
## 목차
1. [설치 및 실행 방법](#1설치-및-실행-방법)
2. [Docker를 사용한 실행](#2docker를-사용한-실행)
3. [기능 사용 방법](#3기능-사용-방법)
4. [API 엔드포인트](#4api-엔드포인트)

---


## 1.설치 및 실행 방법

### 1. 클론 받기

먼저, 이 프로젝트를 GitHub에서 Clone합니다.

```bash
git clone https://github.com/Nanoruri/Sign-UpPage.git
```

### 2. Maven 의존성 다운로드

프로젝트 디렉토리로 이동 후, Maven 의존성을 다운로드합니다.

```bash
mvn install
```

### 3. 애플리케이션 실행 사전 준비

+ **데이터베이스 설정**:
   `application.yml` 파일에서 데이터베이스 설정을 변경할 수 있습니다. 기본적으로 H2 데이터베이스를 사용하며, 다른 데이터베이스로 변경하려면 `application.yml`에서 설정을 수정하십시오.


+ **JWT 토큰 설정**:
   `application.yml` 파일에서 JWT 토큰 설정할 수 있습니다. 설정파일 내 jwt.secret을 추가 하거나 spring.config.import를 수정하여 외부에 있는 설정 파일을 불러오게 할 수 있습니다.

### 4. 애플리케이션 실행 

다음 명령어를 실행하여 애플리케이션을 시작합니다.
```bash
cd springstudy
cd target
java -jar springstudy-1.1.0.jar
```

기본적으로 애플리케이션은 `http://localhost:8082`에서 실행됩니다.


## 2.Docker를 사용한 실행
🚨 **설치 및 실행 방법의 1~3번 과정을 완료한 후, Docker를 사용하여 애플리케이션을 실행할 수 있습니다.**

### 1. Docker 이미지 빌드


Docker를 실행 시킨 후, 다음 명령어를 사용하여 Docker 이미지를 빌드합니다.



```
docker build -t nanoruri/springstudy:1.1.0 .
```



이 명령어는 현재 디렉토리에 있는 Dockerfile을 사용하여 `nanoruri/springstudy:1.0.4`라는 이름의 Docker 이미지를 빌드합니다.


### 2. Docker Compose 실행

프로젝트 루트 디렉토리에 위치한 `docker-compose.yml` 파일을 사용하여 Docker Compose를 통해 애플리케이션을 실행할 수 있습니다.


```
docker-compose up
```



이 명령어는 `docker-compose.yml`에 정의된 서비스들을 시작합니다. 애플리케이션은 `http://localhost:8084`에서 실행됩니다.


⚠️ **주의사항**: Docker Compose명령어를 실행하기 전에 `docker-compose.yml` 파일을 열어 포트를 확인하십시오. 포트가 사용 중인 경우, 다른 포트로 변경하십시오.

⚠️ 실행환경에 대한 설정을 원한다면 `docker-compose.yml` 파일을 수정하여 실행환경을 수정 할 수 있습니다.

---

## 3. 기능 사용 방법

* ###  회원가입

   웹 페이지의 회원가입 페이지에서 이름, 이메일, 비밀번호 등의 정보를 입력하고 회원가입을 진행합니다. 회원가입 후 로그인 페이지로 리다이렉트됩니다.

* ### 4-2 로그인

   이미 회원가입된 사용자는 아이디와 비밀번호를 입력하여 로그인할 수 있습니다.

* ###  게시글 작성

   로그인 후, 게시판 페이지에서 "글 작성" 버튼을 클릭하여 제목과 내용을 입력하고 게시글을 작성합니다.

* ###  게시글 조회

   게시판에서 작성된 게시글을 확인할 수 있습니다. 게시글을 클릭하면 상세보기 페이지로 이동하여 댓글을 작성할 수 있습니다.

* ###  댓글 작성

   게시글 상세보기 페이지에서 "댓글 작성" 버튼을 클릭하고 댓글 내용을 입력하여 댓글을 작성합니다. 작성된 댓글은 다른 사용자도 볼 수 있습니다.

---

## 4. API 엔드포인트

Swagger 사용하여 API 엔드포인트를 확인할 수 있습니다. 애플리케이션을 실행한 후, http://localhost:8082/study/swagger-ui.html 로 이동하여 API 목록을 확인할 수 있습니다.
