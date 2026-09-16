package com.yuseon.ticketing.seat;

import java.util.Optional;

public interface SeatRepository {
	Optional<Seat> findById(Long id);
}
