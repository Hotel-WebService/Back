package com.sw.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomQuantityId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "roomID")
	private Long roomID;

	@Column(name = "date")
	private LocalDate date;
}