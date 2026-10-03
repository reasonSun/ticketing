-- MySQL 8.4 / ticketing 데이터베이스의 최초 테이블 생성용 DDL
-- 실행 전 대상 데이터베이스를 선택한다. 예: USE ticketing;
-- DATETIME(6)은 UTC로 저장하고, 조회 시 애플리케이션에서 표시 시간대로 변환한다.
use ticketing;
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE concerts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    starts_at DATETIME(6) NOT NULL COMMENT '공연 시작 일시 (UTC)',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE seats (
    id BIGINT NOT NULL AUTO_INCREMENT,
    concert_id BIGINT NOT NULL,
    seat_number INT NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_seats_concert_number (concert_id, seat_number)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE reservations (
    id CHAR(36) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '애플리케이션에서 생성한 UUID 문자열',
    user_id BIGINT NOT NULL,
    concert_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    status VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'CONFIRMED',
    reserved_at DATETIME(6) NOT NULL COMMENT '예매 일시 (UTC)',
    canceled_at DATETIME(6) NULL COMMENT '취소 일시 (UTC)',
    PRIMARY KEY (id),
    INDEX idx_reservations_user_concert (user_id, concert_id),
    INDEX idx_reservations_concert_seat (concert_id, seat_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
