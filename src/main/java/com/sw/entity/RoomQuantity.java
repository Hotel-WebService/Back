package com.sw.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "room_quantity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomQuantity {

	@EmbeddedId
	private RoomQuantityId id; // 복합키 (roomID, date)

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roomID", insertable = false, updatable = false)
	@JsonIgnore
	private Room room;

	@Column(name = "total_count")
	private Integer totalCount;

	@Column(name = "reserved_count")
	private Integer reservedCount;

	@Column(name = "available_count", insertable = false, updatable = false)
	private Integer availableCount;

	@Column(name = "created_at", nullable = false, updatable = false, insertable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false)
	private LocalDateTime updatedAt;
}
