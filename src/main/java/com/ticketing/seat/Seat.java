package com.ticketing.seat;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.ticketing.concert.Concert;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "seats", indexes = @Index(name = "idx_seats_concert_number", columnList = "concert_id, seat_number"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "concert_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Concert concert;

	@Column(name = "seat_number", nullable = false)
	private int number;

	public Seat(Concert concert, int number) {
		this(null, concert, number);
	}

	public Seat(Long id, Concert concert, int number) {
		this.id = id;
		this.concert = concert;
		this.number = number;
	}

}
