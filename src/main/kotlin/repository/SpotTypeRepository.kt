package com.sportsocial.repository

import com.sportsocial.model.SpotType
import org.springframework.data.jpa.repository.JpaRepository

interface SpotTypeRepository : JpaRepository<SpotType, Long> {
    fun findByNameContainingIgnoreCase(name: String): List<SpotType>
} 