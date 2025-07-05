package com.sportsocial.dto

import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonProperty

data class SpotPhotoDto(
    val id: Long?,
    val photoUrl: String,
    @JsonProperty("isMain")
    val isMain: Boolean,
    val createdAt: LocalDateTime?
) 