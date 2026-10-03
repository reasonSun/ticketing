# Ticketing

티켓팅 시스템 개발을 위한 초기 Spring Boot 프로젝트입니다. 예매 도메인과 JPA 엔티티, 예매 성공 통합 테스트를 포함합니다. API, 인증 및 결제 기능은 아직 포함하지 않습니다.

## 기술 스택

- Java 21 / Spring Boot 4.1.1
- Gradle Wrapper (Groovy)
- Spring Web MVC / Spring Data JPA
- MySQL 8.4 (Docker Compose)
- 테스트 전용 Docker MySQL 8.4 / JUnit

기본 패키지는 `com.ticketing`입니다.

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

`spring.jpa.open-in-view=false`, `ddl-auto=none`을 사용합니다. 개발 DB의 테이블은 자동 생성하지 않습니다. 최초 생성용 DDL은 `doc/schema.sql`에 있습니다.

## 빌드 및 테스트

```sh
docker compose -f compose.test.yml up -d --wait
./gradlew clean build
```

테스트는 `test` 프로필과 별도 Docker MySQL을 사용합니다. 테스트 DB는 `127.0.0.1:3308/ticketing_test`이며 개발 DB(`3307/ticketing`)와 컨테이너·계정·저장 공간을 분리했습니다. 테스트 계정은 `ticketing_test`, 비밀번호는 `ticketing_test_password`로, 로컬 테스트 전용 값입니다. `.env` 설정 없이 실행할 수 있습니다.

테스트 프로필의 `ddl-auto: create-drop`으로 컨텍스트 시작·종료 시 테스트 테이블을 생성·삭제합니다. 예매 테스트 데이터는 트랜잭션 롤백으로 정리됩니다. 스키마는 JPA 매핑으로 생성하므로 `doc/schema.sql` 자체를 검증하는 테스트는 아닙니다.

예매 테스트만 실행하려면 다음 명령을 사용합니다.

```sh
./gradlew test --tests com.ticketing.reservation.ReservationServiceTest
```

테스트 DB를 종료하려면 다음 명령을 사용합니다. 테스트 데이터는 tmpfs에 저장되므로 컨테이너 정지 시 사라집니다.

```sh
docker compose -f compose.test.yml down
```

실행 가능한 JAR는 `build/libs/ticketing-0.0.1-SNAPSHOT.jar`입니다.

## 종료 및 데이터 보존

애플리케이션은 실행 터미널에서 `Ctrl+C`로 종료합니다.

```sh
docker compose down
```

MySQL 데이터는 Compose 볼륨에 보존됩니다. `docker compose down -v`는 데이터를 삭제하므로 초기화할 때만 사용합니다. 기존 볼륨이 있으면 `.env`의 비밀번호를 바꾸는 것만으로 DB 계정 비밀번호가 변경되지 않습니다.
