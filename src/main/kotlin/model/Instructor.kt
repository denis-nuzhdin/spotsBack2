package com.sportsocial.model

import jakarta.persistence.*

@Entity
@Table(name = "instructors")
data class Instructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val specialization: String,
    val experience: Int,
    val description: String,
    val price: Double,
    val contacts: String
) 