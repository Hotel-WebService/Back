package com.sw.entity;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "hoteltable")
public class Hotel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hotelID")
	private Long hotelID;

	@Column(name = "hotelName", nullable = false, length = 200)
	private String hotelName;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "zip_code")
	private String zipCode;

	@Column(name = "address", length = 300)
	private String address;

	@Column(name = "city", length = 100)
	private String city;

	@Column(name = "district", length = 100)
	private String district;

	@Column(name = "latitude", precision = 12, scale = 10)
	private BigDecimal latitude;

	@Column(name = "longitude", precision = 13, scale = 10)
	private BigDecimal longitude;

	@Column(name = "hotelnumber", length = 50)
	private String hotelnumber;

	@Column(name = "rooms_count")
	private Integer roomsCount;

	@Column(name = "parking_lot", nullable = false)
	private Boolean parkingLot;

	@Column(name = "check_in", columnDefinition = "TIME")
	private LocalTime checkIn;

	@Column(name = "check_out", columnDefinition = "TIME")
	private LocalTime checkOut;

	@Column(name = "star")
	private String star;

	@Column(name = "facilities", length = 255)
	private String facilities;
	
	@Column(name = "price_policy", columnDefinition = "TEXT")
    private String pricePolicy;
	
	public Hotel() {
	}
}