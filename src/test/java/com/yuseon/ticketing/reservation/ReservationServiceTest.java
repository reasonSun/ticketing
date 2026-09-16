package com.yuseon.ticketing.reservation;

import com.yuseon.ticketing.concert.Concert;
import com.yuseon.ticketing.concert.ConcertRepository;
import com.yuseon.ticketing.seat.Seat;
import com.yuseon.ticketing.seat.SeatRepository;
import com.yuseon.ticketing.user.User;
import com.yuseon.ticketing.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationServiceTest {

	@Test
	@DisplayName("등록된 사용자가 공연 시작 전에 빈 좌석을 예매하면 확정된 예매가 저장된다")
	void reservesAnAvailableSeat() {
		// Arrange
		Instant now = Instant.parse("2026-09-16T01:00:00Z");
		Clock clock = Clock.fixed(now, ZoneId.of("Asia/Seoul"));
		User user = new User(7L);
		Concert concert = new Concert(11L, "아이유 콘서트", Instant.parse("2026-10-01T10:00:00Z"));
		Seat seat = new Seat(42L, concert.id(), 1);
		Map<Long, User> users = Map.of(user.id(), user);
		Map<Long, Concert> concerts = Map.of(concert.id(), concert);
		Map<Long, Seat> seats = Map.of(seat.id(), seat);
		UserRepository userRepository = id -> Optional.ofNullable(users.get(id));
		ConcertRepository concertRepository = id -> Optional.ofNullable(concerts.get(id));
		SeatRepository seatRepository = id -> Optional.ofNullable(seats.get(id));
		InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();
		ReservationService sut = new ReservationService(
				userRepository, concertRepository, seatRepository, reservationRepository, clock);

		// Act
		Reservation result = sut.reserve(user.id(), concert.id(), seat.id());

		// Assert
		assertThat(result.id()).isNotNull();
		assertThat(reservationRepository.reservations).hasSize(1);
		Reservation saved = reservationRepository.reservations.get(result.id());
		assertThat(saved).isEqualTo(result);
		assertThat(saved.userId()).isEqualTo(user.id());
		assertThat(saved.concertId()).isEqualTo(concert.id());
		assertThat(saved.seatId()).isEqualTo(seat.id());
		assertThat(saved.status()).isEqualTo(ReservationStatus.CONFIRMED);
		assertThat(saved.reservedAt()).isEqualTo(now);
	}

	private static class InMemoryReservationRepository implements ReservationRepository {
		private final Map<UUID, Reservation> reservations = new HashMap<>();

		@Override
		public Reservation save(Reservation reservation) {
			reservations.put(reservation.id(), reservation);
			return reservation;
		}
	}
}
