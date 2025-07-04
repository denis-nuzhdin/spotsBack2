package com.sportsocial.model

import jakarta.persistence.*

@Entity
@Table(name = "spots")
data class Spot(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    /** Название спота */
    val name: String,
    /** Город */
    val city: String,
    /** Адрес */
    val address: String,
    /** Цена входа */
    val price: Double,
    /** Расписание */
    val schedule: String,
    /** Широта */
    val latitude: Double,
    /** Долгота */
    val longitude: Double
) 