package com.ticketing.reservation;

import com.ticketing.concert.ConcertRepository;
import com.ticketing.seat.SeatRepository;
import com.ticketing.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
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

	@Transactional
	public Reservation reserve(Long userId, Long concertId, Long seatId) {
		var user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
		var concert = concertRepository.findById(concertId)
				.orElseThrow(() -> new IllegalArgumentException("공연이 존재하지 않습니다."));
		var seat = seatRepository.findById(seatId)
				.orElseThrow(() -> new IllegalArgumentException("좌석이 존재하지 않습니다."));

		Reservation reservation = Reservation.confirm(user, concert, seat, clock.instant());
		return reservationRepository.save(reservation);
	}
}
