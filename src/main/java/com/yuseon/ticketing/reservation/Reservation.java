package com.yuseon.ticketing.reservation;

import java.time.Instant;
import java.util.UUID;

public record Reservation(
		UUID id,
		Long userId,
		Long concertId,
		Long seatId,
		ReservationStatus status,
		Instant reservedAt) {

	public static Reservation confirm(Long userId, Long concertId, Long seatId, Instant reservedAt) {
		return new Reservation(UUID.randomUUID(), userId, concertId, seatId,
				ReservationStatus.CONFIRMED, reservedAt);
	}
}
