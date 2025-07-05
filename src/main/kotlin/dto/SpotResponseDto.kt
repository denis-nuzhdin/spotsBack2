package com.sportsocial.dto

data class SpotResponseDto(
    val id: Long,
    val name: String,
    val description: String,
    val city: String,
    val address: String,
    val price: Double,
    val schedule: String,
    val latitude: Double,
    val longitude: Double,
    val spotType: SpotTypeDto,
    val mainPhotoUrl: String?,
    val photos: List<SpotPhotoDto>
) 