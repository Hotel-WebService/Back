package com.sw.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Entity
@Getter @Setter
@Table(name = "consulting_answer")
public class ConsultingAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answer_id;

    @Column(nullable = false)
    private Long user_id; // 연관관계 없이 userID만 저장

    private String district;
    private String star;
    
    @Column(name = "parking_lot")
    private String parking_lot;
    
    private String capacity;
    private String price;
    
    @Column(name = "check_in")
    private String check_in;
    
    private String theme;
    private String mood;
    private String special;

    @Column(name = "answer_time", insertable = false, updatable = false)
    private Timestamp answer_time;
}
 