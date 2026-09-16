# Ticketing

티켓팅 시스템 개발을 위한 초기 Spring Boot 프로젝트입니다. 예매 API, 엔티티, 인증 및 결제 기능은 아직 포함하지 않습니다.

## 기술 스택

- Java 21 / Spring Boot 4.1.1
- Gradle Wrapper (Groovy)
- Spring Web MVC / Spring Data JPA
- MySQL 8.4 (Docker Compose)
- 테스트 전용 H2 / JUnit

기본 패키지는 `com.yuseon.ticketing`입니다.

## 로컬 실행

Java 21과 실행 중인 Docker Desktop(Docker Compose 포함)이 필요합니다.

```sh
cd /Users/yuseon/study/ticketing
cp .env.example .env
```

`.env`의 개발용 비밀번호를 필요에 맞게 수정합니다. `.env`는 Git에 포함되지 않습니다. DB는 `ticketing`, 접속 주소는 `localhost:3307`이며 DB 포트는 로컬에서만 접근할 수 있습니다.

```sh
docker compose up -d --wait
set -a
source .env
set +a
./gradlew bootRun
```

Docker Compose는 `.env`를 자동으로 읽지만 Spring Boot는 자동으로 읽지 않으므로, 위와 같이 환경변수를 내보낸 후 실행합니다. IDE에서는 `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`를 실행 설정의 환경변수에 입력합니다.

서버 포트는 `8080`입니다. 아직 HTTP 엔드포인트가 없어 `/` 요청의 404는 정상입니다. 실행 로그의 `Started TicketingApplication`으로 기동을 확인합니다.

`spring.jpa.open-in-view=false`, `ddl-auto=none`을 사용합니다. 테이블을 자동 생성하지 않으며, 향후 엔티티를 추가할 때 스키마 관리 방식을 정해야 합니다.

## 빌드 및 테스트

```sh
./gradlew clean build
```

기본 컨텍스트 테스트는 `test` 프로필과 메모리 H2를 사용하므로 Docker나 `.env` 없이 실행됩니다. 이 테스트는 MySQL 호환성을 검증하지 않습니다. 실행 가능한 JAR는 `build/libs/ticketing-0.0.1-SNAPSHOT.jar`입니다.

## 종료 및 데이터 보존

애플리케이션은 실행 터미널에서 `Ctrl+C`로 종료합니다.

```sh
docker compose down
```

MySQL 데이터는 Compose 볼륨에 보존됩니다. `docker compose down -v`는 데이터를 삭제하므로 초기화할 때만 사용합니다. 기존 볼륨이 있으면 `.env`의 비밀번호를 바꾸는 것만으로 DB 계정 비밀번호가 변경되지 않습니다.
