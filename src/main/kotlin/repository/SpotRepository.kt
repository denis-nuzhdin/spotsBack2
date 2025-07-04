package com.sportsocial.repository

import com.sportsocial.model.Spot
import org.springframework.data.jpa.repository.JpaRepository

interface SpotRepository : JpaRepository<Spot, Long> {
    fun findByCityContainingIgnoreCaseOrNameContainingIgnoreCase(city: String, name: String): List<Spot>
} 