package com.yuseon.ticketing.concert;

import java.time.Instant;

public record Concert(Long id, String name, Instant startsAt) {
}
