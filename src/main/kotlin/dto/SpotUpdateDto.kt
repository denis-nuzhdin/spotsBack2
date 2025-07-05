package com.sportsocial.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class SpotUpdateDto(
    val name: String? = null,
    val description: String? = null,
    val city: String? = null,
    val address: String? = null,
    val price: Double? = null,
    val schedule: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @field:NotNull(message = "Spot type ID is required")
    @field:Positive(message = "Spot type ID must be positive")
    val spotTypeId: Long,
    val mainPhotoUrl: String? = null
) 