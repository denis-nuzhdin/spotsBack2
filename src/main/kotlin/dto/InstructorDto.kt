package com.sportsocial.dto

import jakarta.validation.constraints.*

data class InstructorDto(
    val id: Long?,
    @field:NotBlank(message = "Name is required")
    val name: String,
    val specialization: String,
    @field:Min(value = 0, message = "Experience must be non-negative")
    val experience: Int,
    val description: String,
    @field:DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    val price: Double,
    @field:NotBlank(message = "Contacts are required")
    val contacts: String
) 