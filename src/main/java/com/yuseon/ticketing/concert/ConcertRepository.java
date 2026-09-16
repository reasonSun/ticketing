package com.yuseon.ticketing.concert;

import java.util.Optional;

public interface ConcertRepository {
	Optional<Concert> findById(Long id);
}
