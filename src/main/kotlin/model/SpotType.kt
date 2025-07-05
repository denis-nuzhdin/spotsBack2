package com.sportsocial.model

import jakarta.persistence.*

@Entity
@Table(name = "spot_types")
data class SpotType(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, length = 255, unique = true)
    val name: String,
    @Column(length = 500)
    val description: String? = null
) 