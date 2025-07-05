package com.sportsocial.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class SpotCreateDto(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    val name: String,
    
    @field:NotBlank(message = "Description is required")
    @field:Size(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters")
    val description: String,
    
    @field:NotBlank(message = "City is required")
    @field:Size(min = 1, max = 255, message = "City must be between 1 and 255 characters")
    val city: String,
    
    @field:NotBlank(message = "Address is required")
    @field:Size(min = 1, max = 255, message = "Address must be between 1 and 255 characters")
    val address: String,
    
    @field:NotNull(message = "Price is required")
    @field:Positive(message = "Price must be positive")
    val price: Double,
    
    @field:NotBlank(message = "Schedule is required")
    @field:Size(min = 1, max = 255, message = "Schedule must be between 1 and 255 characters")
    val schedule: String,
    
    @field:NotNull(message = "Latitude is required")
    val latitude: Double,
    
    @field:NotNull(message = "Longitude is required")
    val longitude: Double,
    
    @field:NotNull(message = "Spot type ID is required")
    @field:Positive(message = "Spot type ID must be positive")
    val spotTypeId: Long,
    
    val mainPhotoUrl: String? = null
) 