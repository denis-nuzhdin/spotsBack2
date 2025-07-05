package com.sportsocial.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SpotTypeDto(
    val id: Long?,
    
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    val name: String,
    
    @field:Size(max = 1000, message = "Description must not exceed 1000 characters")
    val description: String?
) 