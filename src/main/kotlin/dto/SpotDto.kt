package com.sportsocial.dto

data class SpotDto(
    val id: Long?,
    val name: String,
    val city: String,
    val address: String,
    val price: Double,
    val schedule: String,
    val latitude: Double,
    val longitude: Double
) 