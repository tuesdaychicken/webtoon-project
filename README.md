# Webtoon PJ

## 1) 프로젝트 한줄 요약
- 웹툰에 대한 공정한 평가와 작가/독자/편집자 간의 원활한 소통을 위한 웹툰 플랫폼 프로젝트입니다.
- 현재 **사용자 관리(회원가입, 조회, 수정, 탈퇴)** 기능이 구현되어 있습니다.

---

## 2) 기능 조건 (현재 구현)
1. **사용자 가입**
    - 아이디, 비밀번호, 이름, 닉네임, 이메일을 입력받아 회원을 생성합니다.
2. **사용자 조회**
    - 사용자 ID(Username)를 통해 회원 정보를 조회합니다.
3. **사용자 정보 수정**
    - 이름, 이메일, 닉네임을 수정할 수 있습니다.
4. **비밀번호 변경**
    - 새로운 비밀번호로 변경합니다.
5. **사용자 탈퇴**
    - 회원을 삭제합니다.

---

## 3) 개발 환경
### 백엔드 스택
- **언어**: Java
- **프레임워크**: Spring Boot
- **데이터베이스**: Oracle Database (XE)
- **ORM**: JPA (Hibernate)
- **보안**: Spring Security (예정)
- **빌드 도구**: Gradle

### 프론트엔드 스택
- **템플릿 엔진**: Thymeleaf
- **통신**: Fetch API
- **스타일**: CSS Flexbox

---

## 4) 환경설정 및 실행

### 환경설정
`src/main/resources/application-dev.properties` 파일에서 데이터베이스 설정을 확인하세요.

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XE
spring.datasource.username=WEBTOON
spring.datasource.password=1234
```

### 실행 방법
1. Oracle 데이터베이스가 실행 중이어야 합니다.
2. 프로젝트 루트에서 다음 명령어를 실행합니다.

```bash
# Windows
> gradlew bootRun

# Mac/Linux
$ ./gradlew bootRun
```

3. 접속 주소: `http://localhost:8083`

---

## 5) 데이터베이스 모델링
### Table : USERS

| Column Name | Type | Attributes | Description |
| --- | --- | --- | --- |
| ID | NUMBER | PK, Sequence | 유저 시퀀스 번호 |
| USERNAME | VARCHAR2(50) | Unique, Not Null | 로그인 아이디 |
| PASSWORD | VARCHAR2(100) | | 비밀번호 |
| NICKNAME | VARCHAR2(30) | Unique, Not Null | 별명 |
| NAME | VARCHAR2(20) | Not Null | 실명 |
| EMAIL | VARCHAR2(50) | | 이메일 |
| CREATED_AT | TIMESTAMP | Not Null | 가입일 |

---

## 6) API 명세

| Title | Method | Path | Description |
| --- | --- | --- | --- |
| 사용자 가입 | POST | `/api/users` | 신규 회원 등록 |
| 사용자 조회 | GET | `/api/users/{username}` | 회원 정보 단건 조회 |
| 정보 수정 | PATCH | `/api/users/{username}` | 이름, 이메일, 닉네임 수정 |
| 비번 변경 | PATCH | `/api/users/{username}/password` | 비밀번호 변경 |
| 사용자 탈퇴 | DELETE | `/api/users/{username}` | 회원 삭제 |

---

## 7) 구현 내용
### 사용자 관리
- **User Entity**: JPA를 사용하여 DB 테이블과 매핑하였으며, `SequenceGenerator`를 통해 ID를 자동 생성합니다.
- **UserController**: RESTful API 원칙에 따라 자원(User) 중심의 URL을 설계했습니다. (`/api/users`)
- **DTO 패턴**: 요청(`CreateUserRequest`, `UpdateUserRequest`)과 응답(`UserResponse`)을 위한 DTO를 분리하여 엔티티를 직접 노출하지 않도록 설계했습니다.
```