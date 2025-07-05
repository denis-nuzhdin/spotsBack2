package com.sportsocial.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "spot_photos")
data class SpotPhoto(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    val spot: Spot,
    
    val photoUrl: String,
    val isMain: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
) 