package com.ticketing.concert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "concerts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", nullable = false, length = 255)
	private String name;

	@JdbcTypeCode(SqlTypes.TIMESTAMP)
	@Column(name = "starts_at", nullable = false, columnDefinition = "DATETIME(6)")
	private Instant startsAt;

	public Concert(String name, Instant startsAt) {
		this(null, name, startsAt);
	}

	public Concert(Long id, String name, Instant startsAt) {
		this.id = id;
		this.name = name;
		this.startsAt = startsAt;
	}

}
