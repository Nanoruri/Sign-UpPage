gitflow전략을 사용한 스프링 공부용 프로젝트 


# SpringBoot Project

## 1. 프로젝트 소개

+ __프로젝트 명__ : Sign-Up
+ __프로젝트 설명__ : 회원가입 및 로그인에 관련한 기능을 구현한 프로젝트

## 2. 프로젝트 환경
+ __Version__ : Java 11
+ __Framework__ : SpringBoot 2.7.0
+ __ORM__ : JPA
+ __DB__ : MariaDB
+ __Build__ : Maven
+ __Test__ : JUnit5
+ __CI/CD__ : Jenkins
+ __Deploy__ : AWS EC2
+ __VCS__ : Git
+ __VCS-Branch__ : GitFlow
+ __VCS-Repository__ : GitHub
+ __VCS-Commit Message__ : Conventional Commit
+ IDE : IntelliJ IDEA
## 3. 프로젝트 구조
```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── me
│   │   │       └── jh
│   │   │           └── springstudy
│   │   │               ├── config
│   │   │               ├── controller
│   │   │               ├── entity
│   │   │               ├── exception
│   │   │               ├── filter
│   │   │               ├── repositorydao
│   │   │               └── service
│   │   ├── resources
│   │   │   ├── static
│   │   │   │   ├── css
│   │   │   │   └── js
│   │   │   └── templates
│   │   │       ├──error
│   │   │       ├──finds
│   │   │       ├──login
│   │   │       ├──signup
│   │   │       ├──index.html
│   │   └── application.properties
│   └── test
│       └── java
│           └── me
│               └── jh
│                   └── springstudy
│                       ├── controller
│                       ├── entity
│                       ├── repositorydao
│                       └── service
```

## 4. 프로젝트 기능
+ 회원가입
+ 로그인
+ 아이디 찾기
+ 비밀번호 찾기

[//]: # (## 5. 프로젝트 기능 상세)

[//]: # (### 5.1 회원가입)

[//]: # (+ __URL__ : /signup)

[//]: # (+ __Method__ : GET)

[//]: # (+ __Description__ : 회원가입 페이지로 이동)

[//]: # (+ __Parameter__ : 없음)

[//]: # (+ __Return__ : 회원가입 페이지)

[//]: # ()
[//]: # ()
[//]: # (+ __URL__ : /signup)

[//]: # (+ __Method__ : POST)

[//]: # (+ __Description__ : 회원가입)

[//]: # (+ __Parameter__ : )

[//]: # (    + __id__ : 회원 아이디)

[//]: # (    + __password__ : 회원 비밀번호)

[//]: # (    + __name__ : 회원 이름)

[//]: # (    + __email__ : 회원 이메일)

[//]: # (    + __phone__ : 회원 전화번호)

[//]: # (+ __Return__ : 회원가입 성공 페이지)

[//]: # (+ __Exception__ : )

[//]: # (    + __DuplicateIdException__ : 중복된 아이디로 회원가입 시도시 발생)

[//]: # (    + __DuplicateEmailException__ : 중복된 이메일로 회원가입 시도시 발생)

[//]: # (+ __Test__ : )

[//]: # (    + __회원가입 성공__ : 회원가입 성공시 회원가입 성공 페이지로 이동)

[//]: # (    + __아이디 중복__ : 중복된 아이디로 회원가입 시도시 DuplicateIdException 발생)