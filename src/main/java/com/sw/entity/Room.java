package com.sw.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roomtable")
@Getter
@Setter
public class Room {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomID;
	

    private Long hotelID;


	
    private String room_name;
    private Integer price;
    private Integer capacity;
    private String room_description;
    private Boolean room_available;

}
