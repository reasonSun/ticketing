package com.ticketing.reservation;

import com.ticketing.concert.Concert;
import com.ticketing.seat.Seat;
import com.ticketing.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reservations", indexes = {
		@Index(name = "idx_reservations_user_concert", columnList = "user_id, concert_id"),
		@Index(name = "idx_reservations_concert_seat", columnList = "concert_id, seat_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation implements Persistable<UUID> {
	@Id
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(length = 36, columnDefinition = "CHAR(36)")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "concert_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Concert concert;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "seat_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Seat seat;

	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false, length = 16)
	private ReservationStatus status;

	@JdbcTypeCode(SqlTypes.TIMESTAMP)
	@Column(name = "reserved_at", nullable = false, columnDefinition = "DATETIME(6)")
	private Instant reservedAt;

	@JdbcTypeCode(SqlTypes.TIMESTAMP)
	@Column(name = "canceled_at", columnDefinition = "DATETIME(6)")
	private Instant canceledAt;

	// UUID를 저장 전에 할당하므로 Spring Data에 신규 엔티티 여부를 명시한다.
	@Transient
	private boolean newEntity = true;

	public static Reservation confirm(User user, Concert concert, Seat seat, Instant reservedAt) {
		Reservation reservation = new Reservation();
		reservation.id = UUID.randomUUID();
		reservation.user = user;
		reservation.concert = concert;
		reservation.seat = seat;
		reservation.status = ReservationStatus.CONFIRMED;
		reservation.reservedAt = reservedAt;
		return reservation;
	}

	@Override
	public boolean isNew() {
		return newEntity;
	}

	@PostPersist
	@PostLoad
	private void markPersisted() {
		newEntity = false;
	}
}
