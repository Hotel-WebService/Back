package com.sw.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "hotelnumber", length = 50)
    private String hotelnumber;

    @Column(name = "rooms_count")
    private Integer roomsCount;

    @Column(name = "parking_lot", nullable = false)
    private Boolean parkingLot;

    @Column(name = "check_in")
    private LocalDate checkIn;

    @Column(name = "check_out")
    private LocalDate checkOut;

    public Hotel() {
    }
}