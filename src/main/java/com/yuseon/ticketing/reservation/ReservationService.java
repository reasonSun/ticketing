package com.yuseon.ticketing.reservation;

import com.yuseon.ticketing.concert.ConcertRepository;
import com.yuseon.ticketing.seat.SeatRepository;
import com.yuseon.ticketing.user.UserRepository;

import java.time.Clock;

/** 첫 예매 성공 시나리오 구현. 중복 예매와 시간 제한 검증은 후속 단계에서 추가한다. */
public class ReservationService {
	private final UserRepository userRepository;
	private final ConcertRepository concertRepository;
	private final SeatRepository seatRepository;
	private final ReservationRepository reservationRepository;
	private final Clock clock;

	public ReservationService(UserRepository userRepository, ConcertRepository concertRepository,
			SeatRepository seatRepository, ReservationRepository reservationRepository, Clock clock) {
		this.userRepository = userRepository;
		this.concertRepository = concertRepository;
		this.seatRepository = seatRepository;
		this.reservationRepository = reservationRepository;
		this.clock = clock;
	}

	public Reservation reserve(Long userId, Long concertId, Long seatId) {
		var user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
		var concert = concertRepository.findById(concertId)
				.orElseThrow(() -> new IllegalArgumentException("공연이 존재하지 않습니다."));
		var seat = seatRepository.findById(seatId)
				.orElseThrow(() -> new IllegalArgumentException("좌석이 존재하지 않습니다."));

		Reservation reservation = Reservation.confirm(user.id(), concert.id(), seat.id(), clock.instant());
		return reservationRepository.save(reservation);
	}
}
