package com.ticketing.reservation;

import com.ticketing.concert.Concert;
import com.ticketing.concert.ConcertRepository;
import com.ticketing.seat.Seat;
import com.ticketing.seat.SeatRepository;
import com.ticketing.user.User;
import com.ticketing.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

class ReservationServiceTest {

	@Test
	@DisplayName("등록된 사용자가 공연 시작 전에 빈 좌석을 예매하면 확정된 예매가 저장된다")
	void reservesAnAvailableSeat() {
		// Arrange
		Instant now = Instant.parse("2026-09-16T01:00:00Z");
		User user = new User(7L);
		Concert concert = new Concert(11L, "아이유 콘서트", Instant.parse("2026-10-01T10:00:00Z"));
		Seat seat = new Seat(42L, concert, 1);
		ReservationRepository reservationRepository = createReservationRepository();
		ReservationService sut = createReservationService(user, concert, seat, reservationRepository, now);

		// Act
		Reservation result = sut.reserve(user.getId(), concert.getId(), seat.getId());

		// Assert
		verify(reservationRepository).save(result);
		verifyNoMoreInteractions(reservationRepository);
		assertThat(result.getId()).isNotNull();
		assertThat(result.isNew()).isTrue();
		assertThat(result.getUser()).isSameAs(user);
		assertThat(result.getConcert()).isSameAs(concert);
		assertThat(result.getSeat()).isSameAs(seat);
		assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
		assertThat(result.getReservedAt()).isEqualTo(now);
		assertThat(result.getCanceledAt()).isNull();
	}

	private ReservationService createReservationService(User user, Concert concert, Seat seat,
			ReservationRepository reservationRepository, Instant now) {
		UserRepository userRepository = mock(UserRepository.class);
		ConcertRepository concertRepository = mock(ConcertRepository.class);
		SeatRepository seatRepository = mock(SeatRepository.class);
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(concertRepository.findById(concert.getId())).thenReturn(Optional.of(concert));
		when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));
		Clock clock = Clock.fixed(now, ZoneId.of("Asia/Seoul"));

		return new ReservationService(
				userRepository, concertRepository, seatRepository, reservationRepository, clock);
	}

	private ReservationRepository createReservationRepository() {
		ReservationRepository reservationRepository = mock(ReservationRepository.class);
		when(reservationRepository.save(any(Reservation.class))).thenAnswer(returnsFirstArg());
		return reservationRepository;
	}
}
