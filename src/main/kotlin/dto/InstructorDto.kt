package com.sportsocial.dto

data class InstructorDto(
    val id: Long?,
    val name: String,
    val specialization: String,
    val experience: Int,
    val description: String,
    val price: Double,
    val contacts: String
) 