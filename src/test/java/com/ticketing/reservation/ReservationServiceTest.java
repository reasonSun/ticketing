package com.ticketing.reservation;

import com.ticketing.concert.Concert;
import com.ticketing.concert.ConcertRepository;
import com.ticketing.seat.Seat;
import com.ticketing.seat.SeatRepository;
import com.ticketing.user.User;
import com.ticketing.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Import(ReservationServiceTest.FixedClockConfiguration.class)
@Transactional
class ReservationServiceTest {

	private static final Instant NOW = Instant.parse("2026-09-16T01:00:00Z");

	@Autowired
	private ReservationService sut;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ConcertRepository concertRepository;

	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	@DisplayName("등록된 사용자가 공연 시작 전에 빈 좌석을 예매하면 확정된 예매가 DB에 저장된다")
	void reservesAnAvailableSeat() {
		// Arrange
		ReservationFixture fixture = prepareAvailableSeat();

		// Act
		Reservation result = sut.reserve(fixture.userId(), fixture.concertId(), fixture.seatId());
		entityManager.flush();
		entityManager.clear();

		// Assert
		assertThat(result.getId()).isNotNull();
		assertThat(reservationRepository.count()).isEqualTo(1L);
		Reservation saved = reservationRepository.findById(result.getId()).orElseThrow();
		assertThat(saved.getId()).isEqualTo(result.getId());
		assertThat(saved.getUser().getId()).isEqualTo(fixture.userId());
		assertThat(saved.getConcert().getId()).isEqualTo(fixture.concertId());
		assertThat(saved.getSeat().getId()).isEqualTo(fixture.seatId());
		assertThat(saved.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
		assertThat(saved.getReservedAt()).isEqualTo(NOW);
		assertThat(saved.getCanceledAt()).isNull();
	}

	private ReservationFixture prepareAvailableSeat() {
		User user = userRepository.save(new User());
		Concert concert = concertRepository.save(
				new Concert("아이유 콘서트", NOW.plusSeconds(3600)));
		Seat seat = seatRepository.save(new Seat(concert, 1));
		entityManager.flush();
		entityManager.clear();
		return new ReservationFixture(user.getId(), concert.getId(), seat.getId());
	}

	private record ReservationFixture(Long userId, Long concertId, Long seatId) {
	}

	@TestConfiguration(proxyBeanMethods = false)
	static class FixedClockConfiguration {

		@Bean
		@Primary
		Clock fixedClock() {
			return Clock.fixed(NOW, ZoneOffset.UTC);
		}
	}
}
